package com.elice.tripnote.post.exception;


import lombok.Getter;

@Getter
public class NoSuchCommentException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchCommentException(){
        super(ErrorCode.NO_COMMENT.getMessage());
        errorCode = ErrorCode.NO_POST;
    }
}
