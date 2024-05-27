package com.elice.tripnote.domain.hashtag.exception;

import lombok.Getter;

@Getter
public class HashtagNameDuplicateException extends RuntimeException{

    private final ErrorCode errorCode;

    public HashtagNameDuplicateException() {
        super(ErrorCode.DUPLICATE_NAME.getMessage());
        errorCode = ErrorCode.DUPLICATE_NAME;
    }
}
