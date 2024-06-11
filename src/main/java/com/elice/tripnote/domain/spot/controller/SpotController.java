package com.elice.tripnote.domain.spot.controller;


import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDetailDTO;
import com.elice.tripnote.domain.spot.dto.SpotRequestDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/member/spots")
@RequiredArgsConstructor
public class SpotController implements SwaggerSpotController{

    private final SpotService spotService;

    @Override
    @GetMapping
    public ResponseEntity<?> getSpots(@RequestParam(required = true, name="region") Region region,
                                      @RequestParam(required = false, name = "location") String location) {
        if(location ==null)
            return ResponseEntity.ok(spotService.getByRegion(region));
            //return ResponseEntity.ok(spotService.getByRegionAndLocation(region,location));
        return ResponseEntity.ok(spotService.getSpotsByRegionAndLocation(region,location));

    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SpotDetailDTO> getSpotById(@PathVariable("id") Long id){

        return ResponseEntity.ok().body(spotService.getSpotDetails(id));

    }
//    @Override
//    @GetMapping("/{id}")
//    public ResponseEntity<SpotDTO> getSpotById(@PathVariable("id") Long id){
//        try{
//            return ResponseEntity.ok().body(spotService.searchById(id));
//        }catch(LandmarkNotFoundException e){
//            //log.error("에러 발생: {}", e.getMessage(), e);
//            //eturn new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            return ResponseEntity.notFound().build();
//        }
//    }

    @Override
    @PostMapping
    public ResponseEntity<List<Spot>> getSpotForRoute(@RequestBody SpotRequestDTO requestDTO) {
        List<Long> spotIDs = requestDTO.getSpotId();
        return ResponseEntity.ok().body(spotService.getSpotsByIds(spotIDs));
    }

//    @Override
//    @GetMapping("/createRoute")
//    public ResponseEntity<List<Spot>> createRoute(@RequestParam("region") String region) {
//        return spotService.getByRegion(region);
//    }
//
//    @Override
//    @PostMapping
//    public ResponseEntity<Spot> add(@RequestBody SpotRequestDTO spotRequestDTO) {
//        log.info("{}", spotRequestDTO);
//        Spot createdSpot = spotService.add(spotRequestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
//    }
//
//
//    @Override
//    @GetMapping("/spots/address")
//    public ResponseEntity<String> getAddressByCoordinates(@RequestParam(name="lat") double lat, @RequestParam(name="lng") double lng) {
//        return spotService.getAddressByCoordinates(lat, lng);
//    }
}
