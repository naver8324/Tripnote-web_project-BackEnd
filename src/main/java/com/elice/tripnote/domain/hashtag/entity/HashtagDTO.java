package com.elice.tripnote.domain.hashtag.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagDTO {

    private Long id;
    private String name;
    private boolean isCity;
    private boolean isDelete;

}
