package com.elice.tripnote.route.status;

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
