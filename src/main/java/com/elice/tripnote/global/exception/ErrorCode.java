package com.elice.tripnote.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "중복되는 해시태그명입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
