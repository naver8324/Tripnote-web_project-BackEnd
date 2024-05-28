package com.elice.tripnote.global.entity;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private LocalDateTime timestamp;
}
