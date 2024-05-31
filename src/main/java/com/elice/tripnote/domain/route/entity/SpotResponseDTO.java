package com.elice.tripnote.domain.route.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SpotResponseDTO {
    private Long id;
    private String location;
    private String region;
}
