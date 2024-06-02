package com.elice.tripnote.domain.post.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequestDTO {

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;

    @Positive
    private Long contentLength;

}
