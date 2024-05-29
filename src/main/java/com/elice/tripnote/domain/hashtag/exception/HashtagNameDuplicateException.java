package com.elice.tripnote.domain.hashtag.exception;

import com.elice.tripnote.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class HashtagNameDuplicateException extends RuntimeException{

    private final ErrorCode errorCode;

    public HashtagNameDuplicateException() {
        super(ErrorCode.DUPLICATE_NAME.getMessage());
        errorCode = ErrorCode.DUPLICATE_NAME;
    }
}
