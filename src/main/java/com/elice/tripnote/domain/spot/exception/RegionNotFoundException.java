package com.elice.tripnote.domain.spot.exception;

import com.elice.tripnote.global.exception.ErrorCode;
import com.elice.tripnote.global.exception.NotFoundException;
import lombok.Getter;

@Getter
public class RegionNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public RegionNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
