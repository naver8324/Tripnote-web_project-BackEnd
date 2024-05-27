package com.elice.tripnote.domain.spot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpotRequestDTO {
    @NotBlank
    private String region;
}
