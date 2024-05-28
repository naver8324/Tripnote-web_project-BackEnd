package com.elice.tripnote.domain.comment.controller;


import com.elice.tripnote.domain.comment.entity.CommentRequestDTO;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.post.exception.NoSuchAuthorizationException;
import com.elice.tripnote.domain.post.exception.NoSuchCommentException;
import com.elice.tripnote.domain.post.exception.NoSuchPostException;
import com.elice.tripnote.domain.post.exception.NoSuchUserException;
import com.elice.tripnote.global.entity.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController implements SwaggerCommentController {

    private final CommentService commentService;

    @Override
    @GetMapping("/member/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(@RequestParam(name="postId") Long postId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByPostId(postId, page, size));
    }

    @Override
    @GetMapping("/admin/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsAll(@RequestHeader("Authorization") String jwt, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsAll(jwt, page, size));
    }


    @Override
    @GetMapping("/admin/comments/users/{userId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByMemberId(@RequestHeader("Authorization") String jwt, @PathVariable(name = "userId") Long memberId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByMemberId(jwt, memberId, page, size));
    }

    @Override
    @PostMapping("/member/comments")
    public ResponseEntity<CommentResponseDTO> saveComment(@RequestHeader("Authorization") String jwt, @RequestBody CommentRequestDTO commentDTO, @RequestParam(name="postId") Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(jwt, commentDTO, postId));
    }

    @Override
    @PatchMapping("/member/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@RequestHeader("Authorization") String jwt, @RequestBody CommentRequestDTO commentDTO, @PathVariable(name="commentId") Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(jwt, commentDTO, commentId));
    }

    @Override
    @GetMapping("/member/comments/{commentId}/report")
    public ResponseEntity reportComment(@RequestHeader("Authorization") String jwt, @PathVariable(name="commentId") Long commentId) {

        commentService.reportComment(jwt, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @DeleteMapping("/member/comments/{commentId}")
    public ResponseEntity deleteComment(@RequestHeader("Authorization") String jwt, @PathVariable(name="commentId") Long commentId) {
        commentService.deleteComment(jwt, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @DeleteMapping("/admin/comments/{commentId}")
    public ResponseEntity deleteCommentAdmin(@RequestHeader("Authorization") String jwt, @PathVariable(name="commentId") Long commentId) {
        commentService.deleteCommentAdmin(jwt, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
