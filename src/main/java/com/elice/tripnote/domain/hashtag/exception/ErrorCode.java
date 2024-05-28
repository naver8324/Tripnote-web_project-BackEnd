package com.elice.tripnote.domain.hashtag.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_NAME(HttpStatus.CONFLICT, "중복되는 해시태그명입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
