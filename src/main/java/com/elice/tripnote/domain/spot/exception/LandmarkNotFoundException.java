package com.elice.tripnote.domain.spot.exception;

import com.elice.tripnote.global.exception.NotFoundException;

public class LandmarkNotFoundException extends NotFoundException {
    private final static String message = "존재하지 않는 랜드마크입니다.";

    public LandmarkNotFoundException(){
        super(message);
    }
}
