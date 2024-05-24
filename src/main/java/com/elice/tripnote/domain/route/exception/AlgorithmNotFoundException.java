package com.elice.tripnote.domain.route.exception;

import com.elice.tripnote.global.exception.NotFoundException;

public class AlgorithmNotFoundException extends NotFoundException {
    private static final String MESSAGE = "SHA-1 알고리즘을 찾을 수 없습니다.";

    public AlgorithmNotFoundException() {
        super(MESSAGE);
    }
}
