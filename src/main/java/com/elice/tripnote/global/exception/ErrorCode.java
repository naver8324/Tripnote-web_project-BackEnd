package com.elice.tripnote.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 401
    TOKEN_MISSING_OR_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 없거나 적합하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // 404
    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    NO_MEMBER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_SPOT(HttpStatus.NOT_FOUND, "해당하는 여행지가 존재하지 않습니다."),
    NO_ROUTE(HttpStatus.NOT_FOUND, "해당하는 경로가 존재하지 않습니다."),
    NOT_FOUND_ALGORITHM(HttpStatus.NOT_FOUND, "SHA-1 알고리즘을 찾을 수 없습니다."),

    // 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "중복되는 해시태그명입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
