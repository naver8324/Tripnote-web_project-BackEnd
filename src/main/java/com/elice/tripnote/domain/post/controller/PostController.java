package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.comment.entity.CommentRequestDTO;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.exception.*;
import com.elice.tripnote.domain.post.service.PostService;
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
public class PostController implements SwaggerPostController {

    private final PostService postService;
    private final CommentService commentService;

    @Override
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPosts(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPosts(page, size));
    }

    @Override
    @GetMapping("/member/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByMemberId(@RequestHeader("Authorization") String jwt, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByMemberId(jwt, page, size));
    }


    @Override
    @GetMapping("/member/posts/likes")
    public ResponseEntity<Page<PostResponseDTO>> getCommentsByMemberWithLikes(@RequestHeader("Authorization") String jwt, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getCommentsByMemberWithLikes(jwt, page, size));
    }

    @Override
    @GetMapping("/admin/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsAll(@RequestHeader("Authorization") String jwt, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsAll(jwt, page, size));
    }

    @Override
    @GetMapping("/member/posts/{postId}")
    public ResponseEntity<PostDetailResponseDTO> getPost(@RequestHeader("Authorization") String jwt, @PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok().body(postService.getPost(jwt, postId));
    }

    @Override
    @PostMapping("/member/posts")
    public ResponseEntity<PostResponseDTO> savePost(@RequestHeader("Authorization") String jwt,@RequestBody PostRequestDTO postDTO, @RequestParam(name="routeId") Long routeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(jwt, postDTO, routeId));
    }


}
