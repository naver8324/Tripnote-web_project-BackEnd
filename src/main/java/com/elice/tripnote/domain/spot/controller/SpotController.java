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
@RequestMapping("/api/member/spots")
@RequiredArgsConstructor
public class SpotController implements SwaggerSpotController{

    private final SpotService spotService;

    @Override
    @GetMapping
    public ResponseEntity<?> getSpots(@RequestParam(required = false, name="region") String region,
                                      @RequestParam(required = false, name = "location") String location) {
        try{
            if (region != null && location != null) {
                return spotService.getByRegionAndLocation(region,location);
            }
            if(region !=null)
                return spotService.getByRegion(region);
            else
                //return getSpotsByLocations(location);
                return spotService.getByLocation(location);
        }catch(LandmarkNotFoundException | RegionNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
    @GetMapping("/createRoute")
    public ResponseEntity<List<Spot>> createRoute(@RequestParam("region") String region) {
        return spotService.getByRegion(region);
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

    @Override
    @GetMapping("/spots/address")
    public ResponseEntity<String> getAddressByCoordinates(@RequestParam(name="lat") double lat, @RequestParam(name="lng") double lng) {
        return spotService.getAddressByCoordinates(lat, lng);
    }
}
