package com.elice.tripnote.domain.spot.exception;

import com.elice.tripnote.global.exception.NotFoundException;

public class RegionNotFoundException extends NotFoundException {
    private final static String MESSAGE = "존재하지 않는 지역입니다.";

    public RegionNotFoundException(){
        super(MESSAGE);
    }
}
