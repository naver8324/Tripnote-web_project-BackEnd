package com.elice.tripnote.domain.post.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ImageRequestDTO {

    private String fileName;
    private Map<String, String> metaData;
}
