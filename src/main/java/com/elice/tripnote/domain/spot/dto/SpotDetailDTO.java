package com.elice.tripnote.domain.spot.dto;

import com.elice.tripnote.domain.spot.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotDetailDTO {
    private Spot spot;
    private Map<Spot, Double> nextSpots;
}