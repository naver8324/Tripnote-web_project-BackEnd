package com.elice.tripnote.domain.spot.service;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.LandmarkNotFoundException;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.naver.NaverClient;
import com.elice.tripnote.domain.spot.naver.dto.ReverseGeocodeRes;
import com.elice.tripnote.domain.spot.naver.dto.SearchImageReq;
import com.elice.tripnote.domain.spot.naver.dto.SearchLocalReq;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final NaverClient naverClient;


    public ResponseEntity<List<Spot>> getByRegion(String region) {
        Region validRegion = Region.fromString(region);
        List<Spot> list = getSpotsByRegion(validRegion.getName(),0,5);
        if(list.isEmpty())
            throw new RegionNotFoundException();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<Spot> getByLocation(String location) {
        if (searchByLocation(location) != null) {
            return ResponseEntity.ok().body(searchByLocation(location));
        }
        SpotDTO result = search(location);
        if (result == null) {
            throw new LandmarkNotFoundException();
        }
        Spot spot = dtoToEntity(result);
        return ResponseEntity.ok().body(spot);
    }
    public ResponseEntity<List<Spot>> getSpotsByLocations(String location) {
        if (searchByLocations(location).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<SpotDTO> temp = searchByLocations(location);
        List<Spot> list = new ArrayList<>();
        for(SpotDTO s : temp)
            list.add(dtoToEntity(s));
        return ResponseEntity.ok().body(list);
    }
    @Transactional
    public List<Spot> getSpotsByRegion(String region, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());

        if (region == null || region.isEmpty()) {
            return spotRepository.findAll(pageable).getContent();
        } else {
            return spotRepository.findByRegion(region, pageable).getContent();
        }
    }

    @Transactional
    public List<Spot> getSpotsByLocation(String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("likes").descending());

        if (location == null || location.isEmpty()) {
            return spotRepository.findAll(pageable).getContent();
        } else {
            return spotRepository.findByRegion(location, pageable).getContent();
        }
    }

    @Transactional
    public Spot searchById(Long id){
        Spot spot = spotRepository.findById(id).orElseThrow(()->new LandmarkNotFoundException());
        return spot;
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
                    result.setLikes(0);
                    result.setRegion(localItem.getAddress().split(" ")[0]);
                    return result;
                }
            }
        }
        return new SpotDTO();
    }

    @Transactional
    public List<SpotDTO> searchByLocations(String query) {
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);
        if (searchLocalRes.getTotal() > 0) {
            return searchLocalRes.getItems().stream()
                    .filter(localItem -> localItem.getCategory().contains("여행,명소"))
                    .limit(5)
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

                        return new SpotDTO(
                                localItem.getTitle(),
                                0,
                                imageUrl,
                                localItem.getAddress().split(" ")[0]
                        );
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }
    @Transactional
    public Spot add(SpotDTO spotDTO){
        Spot spot = dtoToEntity(spotDTO);
        return spotRepository.save(spot);
    }

    @Transactional
    public Spot dtoToEntity(SpotDTO spotDTO){
        return Spot.builder()
                .location(spotDTO.getLocation())
                .likes(spotDTO.getLikes())
                .imageUrl(spotDTO.getImageUrl())
                .region(spotDTO.getRegion()).build();
    }

    @Transactional
    public SpotDTO EntityToDTO(Spot spot){
        return new SpotDTO(spot.getLocation(), spot.getLikes(), spot.getImageUrl(), spot.getRegion());
    }

    @Transactional
    public Spot searchByLocation(String location) {
        return spotRepository.findByLocation(location).orElse(null);
    }

    @Transactional
    public void increaseLike(String location) {
        Spot spot = spotRepository.findByLocation(location).orElseThrow(() -> new LandmarkNotFoundException());
        spotRepository.increaseLikes(location);
    }


    @Transactional
    public void decreaseLike(String location){
        Spot spot = spotRepository.findByLocation(location).orElseThrow(()->new LandmarkNotFoundException());
        spotRepository.decreaseLikes(location);
        if(spot.getLikes() <0){
            spotRepository.deleteByLocation(location);
        }
    }

    public ResponseEntity<String> getAddressByCoordinates(double lat, double lng) {
        ReverseGeocodeRes res = naverClient.reverseGeocode(lat, lng);
        if (res != null && !res.getResults().isEmpty()) {
            ReverseGeocodeRes.Result result = res.getResults().get(0);
            String address = result.getRegion().getArea1().getName() + " " +
                    result.getRegion().getArea2().getName() + " " +
                    result.getRegion().getArea3().getName();

            if (result.getLand() != null) {
                address += " " + result.getLand().getNumber1();
                if (result.getLand().getNumber2() != null && !result.getLand().getNumber2().isEmpty()) {
                    address += "-" + result.getLand().getNumber2();
                }
            }

            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
        }
    }
}
