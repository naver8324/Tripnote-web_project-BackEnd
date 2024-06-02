package com.elice.tripnote.domain.route.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RouteStatus {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    DELETE("삭제");

    private final String name;
}
