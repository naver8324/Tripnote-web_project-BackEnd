package com.elice.tripnote.domain.route.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveRequestDto {
    private Long memberId;
    private List<Long> spotIds;
    private List<Integer> costs;
    private List<Long> hashtagIds;
}
