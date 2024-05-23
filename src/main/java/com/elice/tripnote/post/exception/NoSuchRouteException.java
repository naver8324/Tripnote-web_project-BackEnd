package com.elice.tripnote.post.exception;


import lombok.Getter;

@Getter
public class NoSuchRouteException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchRouteException(){
        super(ErrorCode.NO_POST.getMessage());
        errorCode = ErrorCode.NO_POST;
    }
}
