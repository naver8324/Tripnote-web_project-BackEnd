package com.elice.tripnote.post.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    NO_ROUTE(HttpStatus.NOT_FOUND, "해당하는 경로는 존재하지 않습니다."),
    NO_COMMENT(HttpStatus.NOT_FOUND, "해당하는 댓글은 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 실행을 수행할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
