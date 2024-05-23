package com.elice.tripnote.post.exception;


import lombok.Getter;

@Getter
public class NoSuchAuthorizationException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchAuthorizationException(){
        super(ErrorCode.UNAUTHORIZED.getMessage());
        errorCode = ErrorCode.NO_POST;
    }
}
