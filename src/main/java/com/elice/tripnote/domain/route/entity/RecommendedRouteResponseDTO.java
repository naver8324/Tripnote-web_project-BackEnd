package com.elice.tripnote.domain.route.entity;

import com.elice.tripnote.domain.spot.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RecommendedRouteResponseDTO {
    private Long integratedRouteId;
    private Long postId;
    private List<Spot> spots; //순서 정리된 채로, (id, region 필요 없음)
    private int likes;
    private boolean likedAt;
    private boolean markedAt;
}
