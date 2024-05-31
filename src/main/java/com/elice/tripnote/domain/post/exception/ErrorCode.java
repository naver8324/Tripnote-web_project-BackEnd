package com.elice.tripnote.domain.post.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_ROUTE(HttpStatus.NOT_FOUND, "해당하는 경로는 존재하지 않습니다."),
    NO_COMMENT(HttpStatus.NOT_FOUND, "해당하는 댓글은 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 실행을 수행할 권한이 없습니다."),
    EXCEED_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "파일 크기가 범위를 넘었습니다."),
    NOT_MATCHED_TYPE(HttpStatus.BAD_REQUEST, "이미지가 아닌 파일입니다."),
    NOT_VALID_ROUTE(HttpStatus.BAD_REQUEST, "이 경로는 비공개, 삭제되었거나 유저의 경로가 아닙니다."),
    NO_INTEGRATED_ROUTE_STATUS(HttpStatus.NOT_FOUND, "해당 지역은 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
