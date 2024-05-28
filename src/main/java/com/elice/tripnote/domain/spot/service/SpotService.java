package com.elice.tripnote.domain.spot.service;

import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.naver.NaverClient;
import com.elice.tripnote.domain.spot.naver.dto.SearchImageReq;
import com.elice.tripnote.domain.spot.naver.dto.SearchLocalReq;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final NaverClient naverClient;

    public List<SpotResponseDTO> getSpotsByRegion(String region) {
        List<Spot> spots;
        if (region == null || region.isEmpty()) {
            spots = spotRepository.findAll();
        } else {
            spots = spotRepository.findByRegion(region);
        }

        return spots.stream()
                .map(spot -> new SpotResponseDTO(
                 //       spot.getId(),
                        spot.getLocation(),
                        spot.getLikes(),
                        spot.getImageUrl(),
                        spot.getRegion()))
                .collect(Collectors.toList());
    }

    public Spot searchById(Long id){
        Spot spot = spotRepository.findById(id).orElse(null);
        if (spot==null) {
            throw new RegionNotFoundException();
        }
        // Assuming you want to return the first spot found
        return spot;
    }
    public SpotDTO search(String query) {
        var searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        var searchLocalRes = naverClient.searchLocal(searchLocalReq);
        if (searchLocalRes.getTotal() > 0) {
            var localItem = searchLocalRes.getItems().stream().findFirst().get();

            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);
            var searchImageRes = naverClient.searchImage(searchImageReq);

            if (searchImageRes.getTotal() > 0) {
                var imageItem = searchImageRes.getItems().stream().findFirst().get();

                var result = new SpotDTO();
                result.setTitle(localItem.getTitle());
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setRoadAddress(localItem.getRoadAddress());
              //  result.setHomePageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }
        }
        return new SpotDTO();
    }

    public Spot add(SpotDTO spotDTO){
        var entity = dtoToEntity(spotDTO);
        spotRepository.save(entity);
        return entity;
    }
    private Spot dtoToEntity(SpotDTO spotDTO){
        String address = spotDTO.getAddress().split(" ")[0];
        return Spot.builder()
                .location(spotDTO.getTitle())
                .likes(spotDTO.getVisitCount())
                .imageUrl(spotDTO.getImageLink())
                .region(address).build();
        //return entity;
    }
}
