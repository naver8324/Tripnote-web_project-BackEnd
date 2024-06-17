package com.elice.tripnote.global.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "페이지네이션 요청 DTO 필요한 값만 넣으면 됩니다.")
public class PageRequestDTO {

    @Schema(description = "페이지 번호 1 이상", defaultValue = "1")
    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private int page;

    @Schema(description = "페이지 크기 1 이상 50 이하", defaultValue = "10")
    @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
    @Max(value = 50, message = "페이지 크기는 50 이하여야 합니다.")
    private int size;

    @Schema(description = "정렬방법", defaultValue = "id")
    private String order; // 기본값 "id"

    @Schema(description = "정렬순서 오름차순(true) 내림차순(false)", defaultValue = "true", allowableValues = {"true", "false"})
    private boolean asc; // 기본값 오름차순

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
        this.order = "id";
        this.asc = true;
    }

}