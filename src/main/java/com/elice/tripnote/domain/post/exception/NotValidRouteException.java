package com.elice.tripnote.domain.post.exception;


import lombok.Getter;

@Getter
public class NotValidRouteException extends RuntimeException{

    private final ErrorCode errorCode;

    public NotValidRouteException(){
        super(ErrorCode.NOT_VALID_ROUTE.getMessage());
        errorCode = ErrorCode.NOT_VALID_ROUTE;
    }
}
