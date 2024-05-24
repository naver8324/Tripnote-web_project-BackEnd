package com.elice.tripnote.domain.route.exception;

import com.elice.tripnote.global.exception.NotFoundException;

public class EntityNotFoundException extends NotFoundException {
    private String MESSAGE;

    public EntityNotFoundException(String MESSAGE) {
        super(MESSAGE);
    }
}
