package com.elice.tripnote.domain.post.entity;


import com.elice.tripnote.domain.route.entity.Route;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostDetailResponseDTO {

    private Long id;

    private String title;

    private String content;

    private int likes;

    private int report;

    private boolean isDeleted;

    private LocalDateTime likedAt;

    private LocalDateTime reportedAt;

    private Route route;

}
