package com.elice.tripnote.domain.integratedroute.exception;

import com.elice.tripnote.global.exception.NotFoundException;

public class EntityNotFoundException extends NotFoundException {
    private String MESSAGE;

    public EntityNotFoundException(String MESSAGE) {
        super(MESSAGE);
    }
}
