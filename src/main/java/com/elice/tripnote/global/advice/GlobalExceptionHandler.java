package com.elice.tripnote.global.advice;


import com.elice.tripnote.domain.hashtag.exception.HashtagNameDuplicateException;
import com.elice.tripnote.domain.post.exception.NoSuchAuthorizationException;
import com.elice.tripnote.domain.post.exception.NoSuchCommentException;
import com.elice.tripnote.domain.post.exception.NoSuchPostException;
import com.elice.tripnote.domain.post.exception.NoSuchUserException;
import com.elice.tripnote.global.entity.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HashtagNameDuplicateException.class)
    public ResponseEntity<ErrorResponse> handelHashtagNameDuplicateException(HashtagNameDuplicateException ex){

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchPostException(NoSuchPostException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchUserException(NoSuchUserException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(NoSuchCommentException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchCommentException(NoSuchCommentException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(NoSuchAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchAuthorizationException(NoSuchAuthorizationException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(errorResponse);
    }


}
