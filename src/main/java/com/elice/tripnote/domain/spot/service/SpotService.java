package com.elice.tripnote.domain.spot.service;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.LandmarkNotFoundException;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.naver.NaverClient;
import com.elice.tripnote.domain.spot.naver.dto.SearchImageReq;
import com.elice.tripnote.domain.spot.naver.dto.SearchLocalReq;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import com.elice.tripnote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpotService {

    private final SpotRepository spotRepository;
    private final NaverClient naverClient;

    public Spot getByRegionAndLocation(String region, String location) {
        Spot spot = spotRepository.findByRegionAndLocation(Region.fromString(region), location); // 변경
        if (spot == null) {
            SpotDTO spotDTO = search(location);
            Spot newSpot = dtoToEntity(spotDTO);
            return spotRepository.save(newSpot);
        }
        return spot;
    }

    public List<Spot> getByRegion(String region) {
        Region validRegion = Region.fromString(region);
        List<Spot> list = getSpotsByRegion(validRegion, 0, 5);
        if (list.isEmpty()){
            log.error("에러 발생: {}", ErrorCode.NO_REGION);
            throw new RegionNotFoundException(ErrorCode.NO_REGION);
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
    public List<SpotResponseDTO> getSpotsByIds(List<Long> spotIDs) {
        List<SpotResponseDTO> list = new ArrayList<>();
        for(Long id : spotIDs){
            Spot spot = spotRepository.findById(id).orElse(null);
            list.add(
                    new SpotResponseDTO(spot.getLocation(), spot.getLat(), spot.getLng())
            );
        }
        return list;
    }
    @Transactional
    public SpotDTO searchById(Long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(() -> new LandmarkNotFoundException());
        SpotDTO spotDTO = EntityToDto(spot);
        return spotDTO;
    }

    @Transactional
    public SpotDTO search(String query) {
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            var localItemOptional = searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행") && localItem.getCategory().contains("명소"))
                    .findFirst();

            if (localItemOptional.isPresent()) {
                var localItem = localItemOptional.get();
                var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
                var searchImageReq = new SearchImageReq();
                searchImageReq.setQuery(imageQuery);
                var searchImageRes = naverClient.searchImage(searchImageReq);

                if (searchImageRes.getTotal() > 0) {
                    var imageItem = searchImageRes.getItems().stream().findFirst().get();

                    var result = new SpotDTO();
                    result.setLocation(localItem.getTitle());
                    result.setImageUrl(imageItem.getLink());
                    result.setRegion(Region.fromString(localItem.getAddress().split(" ")[0])); // 변경
                    result.setAddress(localItem.getAddress());
                    result.setLat(localItem.getMapy() / 1E7);
                    result.setLng(localItem.getMapx() / 1E7);
//                    result.setLat(localItem.getMapx());
//                    result.setLng(localItem.getMapy());
                    return result;
                }
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
//                    .filter(localItem -> localItem.getCategory().contains("여행,명소"))
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
//                        return new SpotDTO(
//                                localItem.getTitle(),
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
