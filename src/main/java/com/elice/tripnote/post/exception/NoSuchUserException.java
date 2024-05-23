package com.elice.tripnote.post.exception;


import lombok.Getter;

@Getter
public class NoSuchUserException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchUserException(){
        super(ErrorCode.NO_COMMENT.getMessage());
        errorCode = ErrorCode.NO_POST;
    }
}
