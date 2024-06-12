package com.elice.tripnote.domain.route.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RouteIdNameResponseDTO {
    private Long postId;
    private Long routeId;
    private String name;
}
