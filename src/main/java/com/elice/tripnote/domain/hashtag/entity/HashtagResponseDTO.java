package com.elice.tripnote.domain.hashtag.entity;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HashtagResponseDTO {

    private Long id;
    private String name;
    private boolean isCity;

}
