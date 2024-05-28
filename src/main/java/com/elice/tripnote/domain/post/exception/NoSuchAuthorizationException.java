package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class NoSuchAuthorizationException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchAuthorizationException(){
        super(ErrorCode.UNAUTHORIZED.getMessage());
        errorCode = ErrorCode.UNAUTHORIZED;
    }
}
