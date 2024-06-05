package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.service.PostService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.annotation.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController implements SwaggerPostController {

    private final PostService postService;

    @Override
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPosts(@RequestParam(name="order", required = false) String order, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPosts(order, page, size));
    }

    @Override
    @PostMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByHashtag(@RequestBody List<HashtagRequestDTO> hashtagRequestDTOList, @RequestParam(name="order", required = false) String order, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByHashtag(hashtagRequestDTOList, order, page, size));
    }


    @Override
    @MemberRole
    @GetMapping("/member/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByMemberId(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByMemberId(page, size));
    }


    @Override
    @MemberRole
    @GetMapping("/member/posts/likes")
    public ResponseEntity<Page<PostResponseDTO>> getCommentsByMemberWithLikes(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getCommentsByMemberWithLikes(page, size));
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/mark")
    public ResponseEntity<Page<PostResponseDTO>> getCommentsByMemberWithMark(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getCommentsByMemberWithMark(page, size));
    }

    @Override
    @AdminRole
    @GetMapping("/admin/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsAll(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsAll(page, size));
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/{postId}")
    public ResponseEntity<PostDetailResponseDTO> getPost(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @Override
    @MemberRole
    @PostMapping("/member/posts")
    public ResponseEntity<PostDetailResponseDTO> savePost(@RequestBody PostRequestDTO postDTO, @RequestParam(name="routeId") Long routeId) {
        Long postId = postService.savePost(postDTO, routeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.getPost(postId));
    }

    @Override
    @MemberRole
    @PatchMapping("/member/posts/{postId}")
    public ResponseEntity<PostDetailResponseDTO> updatePost(@RequestBody PostRequestDTO postDTO, @PathVariable(name="postId") Long postId) {
        postService.updatePost(postDTO, postId);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/{postId}/like")
    public ResponseEntity likePost(@PathVariable(name="postId") Long postId) {
        postService.likePost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/{postId}/mark")
    public ResponseEntity markPost(@PathVariable(name="postId") Long postId) {
        postService.markPost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/{postId}/report")
    public ResponseEntity reportPost(@PathVariable(name="postId") Long postId) {
        postService.reportPost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @MemberRole
    @DeleteMapping("/member/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable(name="postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Override
    @AdminRole
    @DeleteMapping("/admin/posts/{postId}")
    public ResponseEntity deletePostAdmin(@PathVariable(name="postId") Long postId) {
        postService.deletePostAdmin(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
