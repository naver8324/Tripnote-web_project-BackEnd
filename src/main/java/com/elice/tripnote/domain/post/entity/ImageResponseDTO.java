package com.elice.tripnote.domain.post.entity;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {

    @NotBlank
    private String presignedUrl;

    @NotBlank
    private String key;
}
