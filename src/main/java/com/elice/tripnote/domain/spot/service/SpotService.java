package com.elice.tripnote.domain.spot.service;

import com.elice.tripnote.domain.link.routespot.entity.RouteSpot;
import com.elice.tripnote.domain.link.routespot.repository.RouteSpotRepository;
import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotDetailDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.naver.NaverClient;
import com.elice.tripnote.domain.spot.naver.dto.SearchImageReq;
import com.elice.tripnote.domain.spot.naver.dto.SearchLocalReq;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotService {

    private final SpotRepository spotRepository;
    private final NaverClient naverClient;
    private final RouteSpotRepository routespotRepository;
//    public Spot getByRegionAndLocation(Region region, String location) {
////        Spot spot = spotRepository.findByRegionAndLocation(Region.fromString(region), location); // 변경
//        Spot spot = spotRepository.findByRegionAndLocation(region, location);
//        if (spot == null) {
//            SpotDTO spotDTO = search(location);
//            if(spotDTO.getImageUrl()==null){
//                log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
//                throw new LandmarkNotFoundException(ErrorCode.NO_LANDMARK);
//            }
//            Spot newSpot = dtoToEntity(spotDTO);
//            return spotRepository.save(newSpot);
//        }
//        return spot;
//    }
public List<Spot> getByRegionAndLocation(Region region, String location) {
    List<SpotDTO> list = searchByLocations(location);
    List<Spot> spotList = new ArrayList<>();
    for(SpotDTO spotDTO : list){
        if(spotDTO.getImageUrl()==null){
            log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
            throw new CustomException(ErrorCode.NO_LANDMARK);
        }
        if(!region.equals(spotDTO.getRegion())){
            continue; // Skip this Spot and move to the next one
        }
        Spot newSpot = dtoToEntity(spotDTO);
        Spot existingSpot = spotRepository.findByLocation(newSpot.getLocation()).orElse(null);
        if(existingSpot == null){
            Spot temp = spotRepository.save(newSpot);
            spotList.add(temp);
        } else {
            spotList.add(existingSpot);
        }
    }
    if(spotList.isEmpty()){
        log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
        throw new CustomException(ErrorCode.NO_LANDMARK);
    }
    return spotList;
}

    public List<Spot> getByRegion(Region region) {
        //Region validRegion = Region.fromString(region);
        List<Spot> list = getSpotsByRegion(region, 0, 5);
        if (list.isEmpty()){
            log.error("에러 발생: {}", ErrorCode.NO_REGION);
            throw new CustomException(ErrorCode.NO_REGION);
        }
        return list;
    }


    private List<Spot> getSpotsByRegion(Region region, int page, int size) { // 변경
        Pageable pageable = PageRequest.of(page, size);

        if (region == null || region.getName().isEmpty()) { // 변경
            return spotRepository.findAll(pageable).getContent();
        } else {
            return spotRepository.findByRegion(region, pageable).getContent(); // 변경
        }
    }

    @Transactional
    public List<Spot> getSpotsByIds(List<Long> spotIDs) {
        List<Spot> list = new ArrayList<>();
        for(Long id : spotIDs){
            Spot spot = spotRepository.findById(id).orElse(null);
            if(spot == null){
                log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
                throw new CustomException(ErrorCode.NO_LANDMARK);
            }
            list.add(spot);
        }
        return list;
    }
    @Transactional
    public SpotDetailDTO getSpotDetails(Long id) {
        SpotDTO spotDTO = searchById(id);
        Map<Long, Double> nextSpots = calculateNextSpot(id);

        return new SpotDetailDTO(spotDTO, nextSpots);
    }
    @Transactional
    public SpotDTO searchById(Long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NO_LANDMARK));
        SpotDTO spotDTO = EntityToDto(spot);
        return spotDTO;
    }

    @Transactional
    public Map<Long, Double> calculateNextSpot(Long id){
        Map<Long, Double> map = new HashMap<>();
        List<RouteSpot> routeSpots = routespotRepository.findBySpotId(id);
        double total = routeSpots.size()*1.0;
        for(RouteSpot rs : routeSpots){
            map.put(rs.getNextSpotId(), map.getOrDefault(rs.getNextSpotId(),0.0)+1);
        }
        if(map.size()<3){
            return null;
        }
        Map<Long, Double> top3Map = map.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        return top3Map;
    }

    @Transactional
    public SpotDTO search(String query) {
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var localItemOptional = searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행,명소") || localItem.getCategory().contains("음식점") || localItem.getCategory().contains("한식") || localItem.getCategory().contains("술집") || localItem.getCategory().contains("지명"))
                    .findFirst();
            if(!localItemOptional.isPresent()){
                throw new CustomException(ErrorCode.NO_LANDMARK);
            }

            var localItem = localItemOptional.get();
            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);
            var searchImageRes = naverClient.searchImage(searchImageReq);
            if (searchImageRes.getTotal() > 0) {
                var imageItem = searchImageRes.getItems().stream().findFirst().get();

                var result = new SpotDTO();
                var location = localItem.getTitle().replaceAll("<b>","");
                location = location.replaceAll("</b>","");
                //result.setLocation(localItem.getTitle());
                result.setLocation(location);
                result.setImageUrl(imageItem.getLink());
                result.setRegion(Region.fromString(localItem.getAddress().split(" ")[0])); // 변경
                result.setAddress(localItem.getAddress());
                result.setLat(localItem.getMapy() / 1E7);
                result.setLng(localItem.getMapx() / 1E7);
//              result.setLat(localItem.getMapx());
//              result.setLng(localItem.getMapy());
                return result;
            }
        }
        return new SpotDTO();
    }


    @Transactional
    public Spot dtoToEntity(SpotDTO spotDTO) {
        return Spot.builder()
                .location(spotDTO.getLocation())
                .imageUrl(spotDTO.getImageUrl())
                .region(spotDTO.getRegion())
                .address(spotDTO.getAddress())
                .lat(spotDTO.getLat())
                .lng(spotDTO.getLng())
                .build();
    }



    @Transactional
    private SpotDTO EntityToDto(Spot spot) {
        return new SpotDTO(spot.getLocation(), spot.getImageUrl(), spot.getRegion(), spot.getAddress(), spot.getLat(), spot.getLng());
    }

    @Transactional
    public List<SpotDTO> searchByLocations(String query) {
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var filteredItems = searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행,명소") || localItem.getCategory().contains("음식점") || localItem.getCategory().contains("한식") || localItem.getCategory().contains("술집") || localItem.getCategory().contains("지명"))
                    .limit(5)
                    .collect(Collectors.toList());

            if (filteredItems.isEmpty()) {
                throw new CustomException(ErrorCode.NO_LANDMARK);
            }

            return filteredItems.stream()
                    .map(localItem -> {
                        var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
                        var searchImageReq = new SearchImageReq();
                        searchImageReq.setQuery(imageQuery);
                        var searchImageRes = naverClient.searchImage(searchImageReq);

                        String imageUrl = null;
                        if (searchImageRes.getTotal() > 0) {
                            var imageItem = searchImageRes.getItems().stream().findFirst().get();
                            imageUrl = imageItem.getLink();
                        }
                        var location = localItem.getTitle().replaceAll("<b>", "");
                        location = location.replaceAll("</b>", "");
                        return new SpotDTO(
                                location,
                                imageUrl,
                                Region.fromString(localItem.getAddress().split(" ")[0]), // 변경
                                localItem.getAddress(),
                                localItem.getMapy() / 1E7,
                                localItem.getMapx() / 1E7
                        );
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }

//    @Transactional
//    public Spot add(SpotRequestDTO spotRequestDTO) {
//        Region region = Region.fromString(spotRequestDTO.getRegion());
//        Spot spot = Spot.builder()
//                .location(spotRequestDTO.getLocation())
//                .imageUrl(spotRequestDTO.getImageUrl())
//                .region(region)
//                .address(spotRequestDTO.getAddress())
//                .lat(spotRequestDTO.getLat())
//                .lng(spotRequestDTO.getLng())
//                .build();
//        return spotRepository.save(spot);
//    }

//    @Transactional
//    public List<SpotDTO> searchByLocations(String query) {
//        var searchLocalReq = new SearchLocalReq();
//        searchLocalReq.setQuery(query);
//        var searchLocalRes = naverClient.searchLocal(searchLocalReq);
//        if (searchLocalRes.getTotal() > 0) {
//            return searchLocalRes.getItems().stream()
//                    .filter(localItem -> localItem.getCategory().contains("여행,명소") || localItem.getCategory().contains("음식점") || localItem.getCategory().contains("한식") || localItem.getCategory().contains("술집") || localItem.getCategory().contains("지명"))
//                    .limit(5)
//                    .map(localItem -> {
//                        var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
//                        var searchImageReq = new SearchImageReq();
//                        searchImageReq.setQuery(imageQuery);
//                        var searchImageRes = naverClient.searchImage(searchImageReq);
//
//                        String imageUrl = null;
//                        if (searchImageRes.getTotal() > 0) {
//                            var imageItem = searchImageRes.getItems().stream().findFirst().get();
//                            imageUrl = imageItem.getLink();
//                        }
//                        var location = localItem.getTitle().replaceAll("<b>","");
//                        location = location.replaceAll("</b>","");
//                        return new SpotDTO(
//                                location,
//                                imageUrl,
//                                Region.fromString(localItem.getAddress().split(" ")[0]), // 변경
//                                localItem.getAddress(),
//                                localItem.getMapy()/ 1E7,
//                                localItem.getMapx()/ 1E7
//
//                        );
//                    })
//                    .collect(Collectors.toList());
//        }
//        return List.of();
//    }




//    @Transactional
//    public Spot searchByLocation(String location) {
//        return spotRepository.findByLocation(location).orElse(null);
//    }

//    @Transactional
//    public ResponseEntity<String> getAddressByCoordinates(double lat, double lng) {
//        ReverseGeocodeRes res = naverClient.reverseGeocode(lat, lng);
//        if (res != null && !res.getResults().isEmpty()) {
//            ReverseGeocodeRes.Result result = res.getResults().get(0);
//            String address = result.getRegion().getArea1().getName() + " " +
//                    result.getRegion().getArea2().getName() + " " +
//                    result.getRegion().getArea3().getName();
//
//            if (result.getLand() != null) {
//                address += " " + result.getLand().getNumber1();
//                if (result.getLand().getNumber2() != null && !result.getLand().getNumber2().isEmpty()) {
//                    address += "-" + result.getLand().getNumber2();
//                }
//            }
//
//            return new ResponseEntity<>(address, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Transactional
//    public List<Spot> getSpotsByLocation(String location, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        if (location == null || location.isEmpty()) {
//            return spotRepository.findAll(pageable).getContent();
//        } else {
//            return spotRepository.findByLocation(location, pageable).getContent(); // 변경
//        }
//    }



//    public ResponseEntity<Spot> getByLocation(String location) {
//        if (searchByLocation(location) != null) {
//            return ResponseEntity.ok().body(searchByLocation(location));
//        }
//        SpotDTO result = search(location);
//        if (result == null) {
//            throw new LandmarkNotFoundException();
//        }
//        Spot spot = dtoToEntity(result);
//        return ResponseEntity.ok().body(spot);
//    }

//    public ResponseEntity<List<Spot>> getSpotsByLocations(String location) {
//        if (searchByLocations(location).isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        List<SpotDTO> temp = searchByLocations(location);
//        List<Spot> list = new ArrayList<>();
//        for (SpotDTO s : temp)
//            list.add(dtoToEntity(s));
//        return ResponseEntity.ok().body(list);
//    }

}
