package com.elice.tripnote.domain.route.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveRequestDTO {
    @Schema(description = "비용")
    private int expense;

    @Schema(description = "경로 이름", required = true)
    @NotEmpty(message = "경로 이름을 입력하세요.")
    private String name;

    @Schema(description = "여행지 id 리스트", required = true)
    @NotEmpty(message = "여행지가 하나 이상 존재해야합니다.")
    private List<Long> spotIds;

    @Schema(description = "해시태그 id 리스트")
    private List<Long> hashtagIds;
}
