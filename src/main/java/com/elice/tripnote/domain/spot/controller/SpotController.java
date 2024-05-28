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
    public ResponseEntity<?> getSpots(@RequestParam(required = false, name="region") String region,
                                      @RequestParam(required = false, name = "landmark") String landmark) {
        if (region != null && landmark != null) {
            return new ResponseEntity<>("Both region and landmark cannot be specified", HttpStatus.BAD_REQUEST);
        }
        if (region != null) {
            return getSpotsByRegion(region);
        } else if (landmark != null) {
            return getSpotsByLandmark(landmark);
        } else {
            return new ResponseEntity<>("Either region or landmark must be specified", HttpStatus.BAD_REQUEST);
        }
    }
//    private ResponseEntity<SpotDTO> getSpotsByRegion(String region) {
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
//    }
//    private ResponseEntity<List<Spot>> getSpotsByRegion(String region) {
//        try {
//            Region validRegion = Region.fromString(region);
//            List<Spot> list = spotService.getSpotsByRegion(validRegion.getName());
//            if (!list.isEmpty()) {
//                return new ResponseEntity<>(list, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
    private ResponseEntity<List<Spot>> getSpotsByRegion(String region) {
        try {
            Region validRegion = Region.fromString(region);
            List<Spot> list = spotService.getSpotsByRegion(validRegion.getName(), 0, 5);
            if (!list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    private ResponseEntity<Spot> getSpotsByLandmark(String landmark) {
        SpotDTO result = spotService.search(landmark);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Spot spot = spotService.dtoToEntity(result);
        return ResponseEntity.ok().body(spot);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Spot> getSpotById(@PathVariable("id") Long id){
        Spot spot = spotService.searchById(id);
        return ResponseEntity.ok().body(spot);
    }
    @PostMapping("")
    public ResponseEntity<Spot> add(@RequestBody Spot spot) {
        log.info("{}", spot);
        Spot createdSpot = spotService.add(spot);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSpot);
    }
}
