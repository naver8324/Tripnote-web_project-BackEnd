package com.elice.tripnote.domain.comment.controller;


import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.post.exception.NoSuchPostException;
import com.elice.tripnote.domain.post.exception.NoSuchUserException;
import com.elice.tripnote.global.entity.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CommentController implements SwaggerCommentController {

    private final CommentService commentService;

    @Override
    @GetMapping("/api/user/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(@RequestParam(name="postId") Long postId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByPostId(postId, page, size));
    }

    @Override
    @GetMapping("/api/admin/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsAll(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsAll(page, size));
    }

    @Override
    @GetMapping("/api/admin/comments/users/{memberId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByMemberId(@PathVariable(name="memberId") Long memberId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByMemberId(memberId, page, size));
    }


    @ExceptionHandler(NoSuchPostException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchPostException(NoSuchPostException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(404)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchUserException(NoSuchUserException ex){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(404)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
