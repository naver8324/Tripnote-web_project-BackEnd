package com.elice.tripnote.domain.spot.controller;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.LandmarkNotFoundException;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users/spots")
@RequiredArgsConstructor
public class SpotController implements SwaggerSpotController{

    private final SpotService spotService;

    @Override
    @GetMapping
    public ResponseEntity<?> getSpots(@RequestParam(required = false, name="region") String region,
                                      @RequestParam(required = false, name = "location") String location) {
        try{
            if (region != null && location != null) {
                return new ResponseEntity<>("Both region and landmark cannot be specified", HttpStatus.BAD_REQUEST);
            }
            if(region !=null)
                return getSpotsByRegion(region);
            else
                return getSpotsByLocation(location);
        }catch(LandmarkNotFoundException | RegionNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private ResponseEntity<List<Spot>> getSpotsByRegion(String region) {
        Region validRegion = Region.fromString(region);
        List<Spot> list = spotService.getSpotsByRegion(validRegion.getName(),0,5);
        if(list.isEmpty())
            throw new RegionNotFoundException();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    private ResponseEntity<Spot> getSpotsByLocation(String location) {
        if (spotService.searchByLocation(location) != null) {
            return ResponseEntity.ok().body(spotService.searchByLocation(location));
        }
        SpotDTO result = spotService.search(location);
        if (result == null) {
            throw new LandmarkNotFoundException();
        }
        Spot spot = spotService.dtoToEntity(result);
        return ResponseEntity.ok().body(spot);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable("id") Long id){
        try{
            Spot spot = spotService.searchById(id);
            return ResponseEntity.ok().body(spot);
        }catch(LandmarkNotFoundException e){
            log.error("에러 발생: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<Spot> add(@RequestBody SpotDTO spotDTO) {
        log.info("{}", spotDTO);
        Spot createdSpot = spotService.add(spotDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
    }

    @Override
    @PatchMapping("/increaseLikes")
    public ResponseEntity<Void> increaseLike(@RequestParam(name = "location") String location) {
        try {
            spotService.increaseLike(location);
            return ResponseEntity.ok().build();
        } catch (LandmarkNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @PatchMapping("/decreaseLikes")
    public ResponseEntity<Void> decreaseLike(@RequestParam(name = "location") String location) {
        try {
            spotService.decreaseLike(location);
            return ResponseEntity.ok().build();
        } catch (LandmarkNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
