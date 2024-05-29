package com.elice.tripnote.domain.hashtag.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagRequestDTO {

    private String name;

    //JPA로 인해 responseBody에서 city로 값을 받음
    @JsonProperty("city")
    private boolean isCity;

}
