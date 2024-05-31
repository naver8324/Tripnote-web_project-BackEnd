package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class FileTypeNotMatchedException extends RuntimeException{

    private final ErrorCode errorCode;

    public FileTypeNotMatchedException(){
        super(ErrorCode.NOT_MATCHED_TYPE.getMessage());
        errorCode = ErrorCode.NOT_MATCHED_TYPE;
    }
}
