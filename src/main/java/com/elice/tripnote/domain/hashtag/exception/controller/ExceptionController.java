package com.elice.tripnote.domain.hashtag.exception.controller;

import com.elice.tripnote.domain.hashtag.exception.HashtagNameDuplicateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    //예외 처리 메서드
    @ExceptionHandler(HashtagNameDuplicateException.class)
    public ResponseEntity<String> HashtagNameDuplicateExceptionHandler(HashtagNameDuplicateException e){
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(e.getMessage());
    }
}
