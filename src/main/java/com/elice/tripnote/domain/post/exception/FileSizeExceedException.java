package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class FileSizeExceedException extends RuntimeException{

    private final ErrorCode errorCode;

    public FileSizeExceedException(){
        super(ErrorCode.EXCEED_SIZE_LIMIT.getMessage());
        errorCode = ErrorCode.EXCEED_SIZE_LIMIT;
    }
}
