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
    public List<Spot> getSpotsByRegionAndLocation(Region region, String location){
        if(region==Region.ALL)
            return getByALLRegionAndLocation(location);
        return getByRegionAndLocation(region, location);
    }
    private List<Spot> getByRegionAndLocation(Region region, String location) {
        Spot spot = spotRepository.findSpotByRegionAndLocation(region,location);
        List<Spot> spots = spotRepository.findByRegionAndLocation(region,location);
        List<Spot> spotList = new ArrayList<>();
        if(spot !=null){
            spotList.add(spot);
        }
        if(!spotList.isEmpty()){
            for(Spot s : spots){
                if(spotList.contains(s))
                    continue;
                if (region.equals(s.getRegion())) {
                    spotList.add(s);
                }
            }
        }

        if(spotList.size() >5){
            return spotList;
        }
        List<SpotDTO> spotDTOs = searchByLocations(location);
        for (SpotDTO spotDTO : spotDTOs) {
            if(spotList.size() >5)
                break;
            if (spotDTO.getImageUrl() == null) {
                log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
                throw new CustomException(ErrorCode.NO_LANDMARK);
            }
            if (!region.equals(spotDTO.getRegion())) {
                continue; // Skip this Spot and move to the next one
            }
            Spot newSpot = dtoToEntity(spotDTO);
            Spot existingSpot = spotRepository.findByLocation(newSpot.getLocation()).orElse(null);
            if (existingSpot == null) {
                Spot temp = spotRepository.save(newSpot);
                spotList.add(temp);
            } else {
                if(spotList.contains(existingSpot))
                    continue;
                spotList.add(existingSpot);
            }
        }
        if (spotList.isEmpty()) {
            log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
            throw new CustomException(ErrorCode.NO_LANDMARK);
        }
        List<Spot> result = new ArrayList<>();
        for(Spot s : spotList){
            if(s.getRegion().equals(region))
                result.add(s);
        }
        return result;
    }

    private List<Spot> getByALLRegionAndLocation(String location) {
        Spot spot = spotRepository.findSpotAllRegionAndLocation(location);
        List<Spot> spots = spotRepository.findSpotAllRegionAndLocations(location);
        List<Spot> spotList = new ArrayList<>();
        if(spot !=null){
            spotList.add(spot);
        }
        if(!spotList.isEmpty()){
            for(Spot s : spots){
                if(spotList.contains(s))
                    continue;
                spotList.add(s);
            }
        }

        if(spotList.size() >5){
            return spotList;
        }
        List<SpotDTO> spotDTOs = searchByLocations(location);
        for (SpotDTO spotDTO : spotDTOs) {
            if(spotList.size() >5)
                break;
            if (spotDTO.getImageUrl() == null) {
                log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
                throw new CustomException(ErrorCode.NO_LANDMARK);
            }
            Spot newSpot = dtoToEntity(spotDTO);
            Spot existingSpot = spotRepository.findByLocation(newSpot.getLocation()).orElse(null);
            if (existingSpot == null) {
                Spot temp = spotRepository.save(newSpot);
                spotList.add(temp);
            } else {
                if(spotList.contains(existingSpot))
                    continue;
                spotList.add(existingSpot);
            }
        }
        if (spotList.isEmpty()) {
            log.error("에러 발생: {}", ErrorCode.NO_LANDMARK);
            throw new CustomException(ErrorCode.NO_LANDMARK);
        }

        return spotList;
    }


    public List<Spot> getByRegion(Region region) {
        if (region == Region.ALL) {
            List<Spot> allSpots = spotRepository.findAll();
            return allSpots.stream()
                    .limit(5)  // 상위 5개만 가져옴
                    .collect(Collectors.toList());
            //return spotRepository.findTop5ByOrderByLocationAsc();
        }
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
        Spot spot = spotRepository.findById(id).orElse(null);
        if(spot == null){
            throw new CustomException(ErrorCode.NO_LANDMARK);
        }
        Map<Spot, Double> nextSpots = calculateNextSpot(id);

        return new SpotDetailDTO(spot, nextSpots);
    }
    @Transactional
    public SpotDTO searchById(Long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NO_LANDMARK));
        SpotDTO spotDTO = EntityToDto(spot);
        return spotDTO;
    }

    @Transactional
    public Map<Spot, Double> calculateNextSpot(Long id){
        Map<Spot, Double> map = new HashMap<>();
        List<RouteSpot> routeSpots = routespotRepository.findBySpotId(id);
        double total = routeSpots.size()*1.0;
        for(RouteSpot rs : routeSpots){
            Spot nextSpot = spotRepository.findById(rs.getNextSpotId()).orElse(null);
            if(nextSpot==null){
                continue;
            }
            map.put(nextSpot, map.getOrDefault(nextSpot,0.0)+1.0);
        }
        if(map.size()<3){
            return null;
        }
        for(Spot key : map.keySet()){
            map.put(key, map.get(key) / total);
        }
        Map<Spot, Double> top3Map = map.entrySet()
                .stream()
                .sorted(Map.Entry.<Spot, Double>comparingByValue().reversed())
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
        //searchLocalReq.setQuery(query);
        searchLocalReq.setNewQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var localItemOptional = searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행,명소") || localItem.getCategory().contains("음식점") || localItem.getCategory().contains("한식") || localItem.getCategory().contains("술집") || localItem.getCategory().contains("지명") || localItem.getCategory().contains("육류") || localItem.getCategory().contains("문화,예술") || localItem.getCategory().contains("쇼핑,유통"))
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
                var location = localItem.getTitle();
               // location = localItem.getTitle().replaceAll("<b>","");
               // location = location.replaceAll("</b>","");
                //result.setLocation(localItem.getTitle());
                location=location.trim();
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
        //searchLocalReq.setQuery(query);
        searchLocalReq.setNewQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var filteredItems = searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행,명소") || localItem.getCategory().contains("음식점") || localItem.getCategory().contains("한식") || localItem.getCategory().contains("술집") || localItem.getCategory().contains("지명") || localItem.getCategory().contains("육류") || localItem.getCategory().contains("문화,예술") ||  localItem.getCategory().contains("쇼핑,유통"))
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
                        location=location.trim();
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

}
