package com.elice.tripnote.domain.post.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDTO {

    private String presignedUrl;
    private String key;
}
