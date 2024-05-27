package com.elice.tripnote.domain.spot.controller;

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
        //return spotService.getSpotsByRegion(region);
        SpotDTO result = spotService.search(region);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("")
    public Spot add(@RequestBody SpotDTO spotDTO) {
        log.info("{}", spotDTO);

        return spotService.add(spotDTO);
    }
}
