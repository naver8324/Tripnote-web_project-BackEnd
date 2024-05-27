package com.elice.tripnote.domain.spot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpotResponseDTO {
    //private Long id;
    private String location;
    private int likes;
    private String imageUrl;
    private String region;

}
