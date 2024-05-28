package com.elice.tripnote.domain.spot.controller;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.service.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    @GetMapping
    public ResponseEntity<SpotDTO> getSpotsByRegion(@RequestParam(name = "region",required = false) String region) {
        SpotDTO result = spotService.search(region);
        if (result != null) {
            //return new ResponseEntity<>(result, HttpStatus.OK);
            return ResponseEntity.ok().body(result);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //return spotService.getSpotsByRegion(region);
//        try {
//            Region validRegion = Region.fromString(region);
//            SpotDTO result = spotService.search(validRegion.getName());
//            if (result != null) {
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable("id") Long id){
        Spot spot = spotService.searchById(id);
        return ResponseEntity.ok().body(spot);
    }
    @PostMapping("")
    public ResponseEntity<Spot> add(@RequestBody SpotDTO spotDTO) {
        log.info("{}", spotDTO);
        Spot createdSpot = spotService.add(spotDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
    }
}
