package com.elice.tripnote.domain.route.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpotResponseDTO {
    private String location;
    private double lat;
    private double lng;
}
