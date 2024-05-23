package com.elice.tripnote.hashtag.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HashtagResponseDTO {

    private Long id;
    private String name;
    private String isCity;

}
