package com.elice.tripnote.domain.member.exception;

import com.elice.tripnote.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CustomDuplicateException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomDuplicateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}