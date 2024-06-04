package com.elice.tripnote.domain.spot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpotResponseDTO {
    private String location;
    private double lat;
    private double lng;
}
