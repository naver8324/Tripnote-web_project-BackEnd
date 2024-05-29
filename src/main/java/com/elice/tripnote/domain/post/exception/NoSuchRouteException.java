package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class NoSuchRouteException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchRouteException(){
        super(ErrorCode.NO_ROUTE.getMessage());
        errorCode = ErrorCode.NO_ROUTE;
    }
}
