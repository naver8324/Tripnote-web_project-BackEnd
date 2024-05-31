package com.elice.tripnote.global.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private String errorCode;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private List<FieldError> validation;



    @Getter
    @Setter
    @NoArgsConstructor
    public static class FieldError{
        private String field;
        private String message;
    }

}
