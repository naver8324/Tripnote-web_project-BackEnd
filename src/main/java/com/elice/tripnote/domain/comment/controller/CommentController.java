package com.elice.tripnote.domain.comment.controller;


import com.elice.tripnote.domain.comment.entity.CommentRequestDTO;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.annotation.MemberRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController implements SwaggerCommentController {

    private final CommentService commentService;

    @Override
    @MemberRole
    @GetMapping("/member/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(@RequestParam(name="postId") Long postId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByPostId(postId, page, size));
    }

    @Override
    @AdminRole
    @GetMapping("/admin/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsAll(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsAll(page, size));
    }


    @Override
    @AdminRole
    @GetMapping("/admin/comments/members/{memberId}")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByMemberId( @PathVariable(name = "memberId") Long memberId, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(commentService.getCommentsByMemberId(memberId, page, size));
    }

    @Override
    @MemberRole
    @PostMapping("/member/comments")
    public ResponseEntity<CommentResponseDTO> saveComment(@Valid @RequestBody CommentRequestDTO commentDTO, @RequestParam(name="postId") Long postId) {
        commentService.saveComment(commentDTO, postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.saveComment(commentDTO, postId));
    }

    @Override
    @MemberRole
    @PatchMapping("/member/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@Valid @RequestBody CommentRequestDTO commentDTO, @PathVariable(name="commentId") Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(commentDTO, commentId));
    }

    @Override
    @MemberRole
    @GetMapping("/member/comments/{commentId}/report")
    public ResponseEntity reportComment(@PathVariable(name="commentId") Long commentId) {

        commentService.reportComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @MemberRole
    @DeleteMapping("/member/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable(name="commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @AdminRole
    @DeleteMapping("/admin/comments/{commentId}")
    public ResponseEntity deleteCommentAdmin(@PathVariable(name="commentId") Long commentId) {
        commentService.deleteCommentAdmin(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
