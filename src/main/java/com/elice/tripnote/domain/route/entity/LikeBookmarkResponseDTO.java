package com.elice.tripnote.domain.route.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class LikeBookmarkResponseDTO {
    private int likes;
    private int bookmark;
}
