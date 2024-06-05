package com.elice.tripnote.domain.route.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class SaveRequestDTO {
    private int expense;
    private String name;
    @NotEmpty(message = "여행지가 하나 이상 존재해야합니다.")
    private List<Long> spotIds;
    private List<Long> hashtagIds;
}
