package com.elice.tripnote.domain.integratedroute.exception;


import com.elice.tripnote.domain.post.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchIntegratedRouteStatusException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchIntegratedRouteStatusException(){
        super(ErrorCode.NO_ROUTE.getMessage());
        errorCode = ErrorCode.NO_ROUTE;
    }
}
