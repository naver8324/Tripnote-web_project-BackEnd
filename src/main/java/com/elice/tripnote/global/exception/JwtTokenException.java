package com.elice.tripnote.global.exception;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException{

    private final ErrorCode errorCode;

    public JwtTokenException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
