package com.elice.tripnote.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400
    EXCEED_SIZE_LIMIT(HttpStatus.BAD_REQUEST, "파일 크기가 범위를 넘었습니다."),
    NOT_MATCHED_TYPE(HttpStatus.BAD_REQUEST, "이미지가 아닌 파일입니다."),
    NOT_VALID_ROUTE(HttpStatus.BAD_REQUEST, "이 경로는 비공개, 삭제되었거나 유저의 경로가 아닙니다."),
    REDIRECT_URI_MISMATCH(HttpStatus.BAD_REQUEST, "인가 코드 요청시 사용한 redirect uri와 액세스 토큰 요청 시 사용한 redirect uri가 다릅니다."),
    TOO_MANY_ARGUMENT(HttpStatus.BAD_REQUEST, "너무 많은 인자 값을 넣었습니다."),
    SOCIAL_LOGIN_EMAIL(HttpStatus.BAD_REQUEST, "소셜로 로그인 된 이메일입니다."),

    // 401
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    TOKEN_MISSING_OR_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 없거나 적합하지 않습니다."),
    KAKAO_TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "카카오 엑세스 토큰이 없습니다."),
    KAKAO_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "카카오 엑세스 토큰이 적합하지 않습니다."),
    NOT_EXIST_APP_KEY(HttpStatus.UNAUTHORIZED, "잘못된 앱 키 타입을 사용하거나 앱 키에 오타가 있는 것 같습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "해당 실행을 수행할 권한이 없습니다."),
    DELETED_BY_USER(HttpStatus.UNAUTHORIZED, "탈퇴한 아이디입니다."),
    DELETED_BY_ADMIN(HttpStatus.UNAUTHORIZED, "관리자에 의해 탈퇴된 아이디입니다."),
    INVALID_AUTH_CODE(HttpStatus.UNAUTHORIZED, "인증 코드가 올바르지 않습니다."),
    JSON_PARSING_ERROR(HttpStatus.UNAUTHORIZED, "JSON 파싱 중 오류가 발생했습니다"),

    //403
    UNAUTHORIZED_DELETE(HttpStatus.FORBIDDEN, "삭제할 권한이 없습니다."),
    UNAUTHORIZED_UPDATE_STATUS(HttpStatus.FORBIDDEN, "상태를 변경할 권한이 없습니다."),
    UNAUTHORIZED_UPDATE_NAME(HttpStatus.FORBIDDEN, "이름을 변경할 권한이 없습니다."),

    // 404
    NO_POST(HttpStatus.NOT_FOUND, "해당하는 게시글은 존재하지 않습니다."),
    NO_MEMBER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_SPOT(HttpStatus.NOT_FOUND, "해당하는 여행지가 존재하지 않습니다."),
    NO_ROUTE(HttpStatus.NOT_FOUND, "해당하는 경로가 존재하지 않습니다."),
    NOT_FOUND_ALGORITHM(HttpStatus.NOT_FOUND, "SHA-1 알고리즘을 찾을 수 없습니다."),
    NO_USER(HttpStatus.NOT_FOUND, "해당하는 유저는 존재하지 않습니다."),
    NO_EMAIL(HttpStatus.NOT_FOUND, "해당하는 이메일은 존재하지 않습니다."),
    NO_COMMENT(HttpStatus.NOT_FOUND, "해당하는 댓글은 존재하지 않습니다."),
    NO_INTEGRATED_ROUTE_STATUS(HttpStatus.NOT_FOUND, "해당 지역은 존재하지 않습니다."),
    NO_LANDMARK(HttpStatus.NOT_FOUND, "존재하지 않는 랜드마크입니다."),
    NO_REGION(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다."),
    NO_LIKE_BOOKMARK_PERIOD(HttpStatus.NOT_FOUND, "해당하는 기간별 좋아요 북마크 객체가 존재하지 않습니다."),
    MEMBER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 삭제된 회원입니다."),
    MEMBER_ALREADY_RESTORED(HttpStatus.NOT_FOUND, "이미 복구된 회원입니다."),

    // 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_NAME(HttpStatus.CONFLICT, "이미 존재하는 해시태그명입니다."),

    // 500
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 응답 오류입니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
