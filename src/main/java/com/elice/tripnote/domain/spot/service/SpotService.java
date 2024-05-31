package com.elice.tripnote.domain.spot.service;

import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.LandmarkNotFoundException;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.naver.NaverClient;
import com.elice.tripnote.domain.spot.naver.dto.SearchImageReq;
import com.elice.tripnote.domain.spot.naver.dto.SearchLocalReq;
import com.elice.tripnote.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpotService {

    private final SpotRepository spotRepository;
    private final NaverClient naverClient;

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
            var localItem = searchLocalRes.getItems().stream().findFirst().get();

            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
            var searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);
            var searchImageRes = naverClient.searchImage(searchImageReq);

            if (searchImageRes.getTotal() > 0) {
                var imageItem = searchImageRes.getItems().stream().findFirst().get();

                var result = new SpotDTO();
                result.setLocation(localItem.getTitle());
                result.setImageUrl(localItem.getLink());
                result.setLikes(0);
                result.setRegion(localItem.getAddress().split(" ")[0]);
                return result;
            }
        }
        return new SpotDTO();
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
}
