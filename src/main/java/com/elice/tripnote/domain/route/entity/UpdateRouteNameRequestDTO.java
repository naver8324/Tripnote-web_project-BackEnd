package com.elice.tripnote.domain.route.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRouteNameRequestDTO {
    @NotEmpty(message = "이름을 바꿀 경로의 id를 입력하세요.")
    private Long routeId;
    @NotEmpty(message = "새로운 경로 이름을 입력하세요.")
    private String name;
}
