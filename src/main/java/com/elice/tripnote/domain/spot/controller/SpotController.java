package com.elice.tripnote.domain.spot.controller;


import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotRequestDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.exception.LandmarkNotFoundException;
import com.elice.tripnote.domain.spot.exception.RegionNotFoundException;
import com.elice.tripnote.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<?> getSpots(@RequestParam(required = true, name="region") String region,
                                      @RequestParam(required = false, name = "location") String location) {
        try{
            if(location ==null)
                return ResponseEntity.ok(spotService.getByRegion(region));
            return ResponseEntity.ok(spotService.getByRegionAndLocation(region,location));
        }catch(LandmarkNotFoundException | RegionNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<SpotDTO> getSpotById(@PathVariable("id") Long id){
        try{
            return ResponseEntity.ok().body(spotService.searchById(id));
        }catch(LandmarkNotFoundException e){
            //log.error("에러 발생: {}", e.getMessage(), e);
            //eturn new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PostMapping
    public ResponseEntity<List<SpotResponseDTO>> getSpotForRoute(@RequestBody SpotRequestDTO requestDTO) {
        List<Long> spotIDs = requestDTO.getSpotId();
        if (spotIDs == null || spotIDs.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 필수 파라미터가 없는 경우 400 Bad Request 반환
        }

        // 클라이언트가 선택한 Spot의 ID를 기반으로 해당 Spot들을 조회하는 메서드 호출
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
