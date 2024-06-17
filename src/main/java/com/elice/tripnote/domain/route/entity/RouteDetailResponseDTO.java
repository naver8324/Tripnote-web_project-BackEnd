package com.elice.tripnote.domain.route.entity;

import com.elice.tripnote.domain.spot.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class RouteDetailResponseDTO {
    private Long routeId;
    private Long postId;
    private String name;
    private List<Spot> spots; // 순서대로
}
