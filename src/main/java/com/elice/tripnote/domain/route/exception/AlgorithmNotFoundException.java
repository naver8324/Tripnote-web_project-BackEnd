package com.elice.tripnote.domain.route.exception;

import com.elice.tripnote.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class AlgorithmNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public AlgorithmNotFoundException(){
        super(ErrorCode.NOT_FOUND_ALGORITHM.getMessage());
        this.errorCode = ErrorCode.NOT_FOUND_ALGORITHM;
    }
}
