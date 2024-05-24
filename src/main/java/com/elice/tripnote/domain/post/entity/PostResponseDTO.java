package com.elice.tripnote.domain.post.entity;


import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostResponseDTO{

    private Long id;

    private String title;

    private String content;

    private int likes;

    private int report;

    private boolean isDeleted;

}
