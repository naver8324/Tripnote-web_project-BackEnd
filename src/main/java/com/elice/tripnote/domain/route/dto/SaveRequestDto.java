package com.elice.tripnote.domain.route.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveRequestDto {
    private Long memberId;
    private int expense;
    private List<Long> spotIds;
    private List<Long> hashtagIds;
}
