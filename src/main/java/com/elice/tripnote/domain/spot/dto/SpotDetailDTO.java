package com.elice.tripnote.domain.spot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotDetailDTO {
    private SpotDTO spot;
    private Map<Long, Double> nextSpots;
}