package com.elice.tripnote.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class NoSuchPostException extends RuntimeException{

    private final ErrorCode errorCode;

    public NoSuchPostException(){
        super(ErrorCode.NO_POST.getMessage());
        errorCode = ErrorCode.NO_POST;
    }
}
