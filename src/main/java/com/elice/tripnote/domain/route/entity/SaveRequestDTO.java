package com.elice.tripnote.domain.route.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class SaveRequestDTO {
    private Long memberId;
    private int expense;
    private List<Long> spotIds;
    private List<Long> hashtagIds;
}
