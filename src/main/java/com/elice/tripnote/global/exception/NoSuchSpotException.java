package com.elice.tripnote.global.exception;

import com.elice.tripnote.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class NoSuchSpotException extends RuntimeException{
    private final ErrorCode errorCode;

    public NoSuchSpotException(){
        super(ErrorCode.NO_SPOT.getMessage());
        this.errorCode = ErrorCode.NO_SPOT;
    }
}
