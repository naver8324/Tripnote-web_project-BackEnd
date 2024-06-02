package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController implements SwaggerPostController {

    private final PostService postService;

    @Override
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPosts(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPosts(page, size));
    }

    @Override
    @GetMapping("/member/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByMemberId(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByMemberId(page, size));
    }


    @Override
    @GetMapping("/member/posts/likes")
    public ResponseEntity<Page<PostResponseDTO>> getCommentsByMemberWithLikes(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getCommentsByMemberWithLikes(page, size));
    }

    @Override
    @GetMapping("/member/posts/mark")
    public ResponseEntity<Page<PostResponseDTO>> getCommentsByMemberWithMark(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getCommentsByMemberWithMark(page, size));
    }

    @Override
    @GetMapping("/admin/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsAll(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsAll(page, size));
    }

    @Override
    @GetMapping("/member/posts/{postId}")
    public ResponseEntity<PostDetailResponseDTO> getPost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @Override
    @PostMapping("/member/posts")
    public ResponseEntity<PostResponseDTO> savePost(@RequestBody PostRequestDTO postDTO, @RequestParam(name="routeId") Long routeId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.savePost(postDTO, routeId));
    }

    @Override
    @PatchMapping("/member/posts/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@RequestBody PostRequestDTO postDTO, @PathVariable(name="postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(postDTO, postId));
    }

    @Override
    @GetMapping("/member/posts/{postId}/like")
    public ResponseEntity likePost(@PathVariable(name="postId") Long postId) {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @GetMapping("/member/posts/{postId}/mark")
    public ResponseEntity markPost(@PathVariable(name="postId") Long postId) {
        postService.markPost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @GetMapping("/member/posts/{postId}/report")
    public ResponseEntity reportPost(@PathVariable(name="postId") Long postId) {
        postService.reportPost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @DeleteMapping("/member/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable(name="postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Override
    @DeleteMapping("/admin/posts/{postId}")
    public ResponseEntity deletePostAdmin(@PathVariable(name="postId") Long postId) {
        postService.deletePostAdmin(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
