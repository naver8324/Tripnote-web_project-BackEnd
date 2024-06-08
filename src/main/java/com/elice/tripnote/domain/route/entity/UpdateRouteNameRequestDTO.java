package com.elice.tripnote.domain.route.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateRouteNameRequestDTO {
    @Schema(description = "이름을 바꿀 경로의 id")
    @NotEmpty(message = "이름을 바꿀 경로의 id를 입력하세요.")
    private Long routeId;
    @Schema(description = "새로운 경로 이름")
    @NotEmpty(message = "새로운 경로 이름을 입력하세요.")
    private String name;
}
