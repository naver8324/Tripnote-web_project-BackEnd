package com.elice.tripnote.post.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRequestDTO {

    private Long id;

    private String title;

    private String content;

}
