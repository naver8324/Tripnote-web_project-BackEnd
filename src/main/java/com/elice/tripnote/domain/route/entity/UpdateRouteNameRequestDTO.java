package com.elice.tripnote.domain.route.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRouteNameRequestDTO {
    private Long routeId;
    private String name;
}
