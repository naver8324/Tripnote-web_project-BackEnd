package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class NoSuchFileException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchFileException(){
        super(ErrorCode.NO_USER.getMessage());
        errorCode = ErrorCode.NO_USER;
    }
}
