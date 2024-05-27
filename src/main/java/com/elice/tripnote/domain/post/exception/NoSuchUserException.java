package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class NoSuchUserException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchUserException(){
        super(ErrorCode.NO_USER.getMessage());
        errorCode = ErrorCode.NO_USER;
    }
}
