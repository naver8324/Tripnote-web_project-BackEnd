package com.elice.tripnote.domain.post.controller;


import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.service.PostService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.annotation.MemberRole;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController implements SwaggerPostController {

    private final PostService postService;

    @Override
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPosts(@RequestParam(name="order", required = false, defaultValue = "") String order, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPosts(order, page, size));
    }

    @Override
    @PostMapping("/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByHashtag(@Valid @RequestBody List<HashtagRequestDTO> hashtagRequestDTOList, @RequestParam(name="order", required = false, defaultValue = "") String order, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {

        if(hashtagRequestDTOList == null){
            return ResponseEntity.ok().body(postService.getPosts(order, page, size));

        }
        if(hashtagRequestDTOList.isEmpty()){
            return ResponseEntity.ok().body(postService.getPosts(order, page, size));
        }
        for (HashtagRequestDTO hashtagRequestDTO : hashtagRequestDTOList) {
            if(hashtagRequestDTO.getName().equals("전체")){
                return ResponseEntity.ok().body(postService.getPosts(order, page, size));

            }
        }

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
    public ResponseEntity<Page<PostResponseDTO>> getPostsByMemberWithLikes(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByMemberWithLikes(page, size));
    }

    @Override
    @MemberRole
    @GetMapping("/member/posts/mark")
    public ResponseEntity<Page<PostResponseDTO>> getPostsByMemberWithMark(@RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        return ResponseEntity.ok().body(postService.getPostsByMemberWithMark(page, size));
    }

    @Override
    @AdminRole
    @GetMapping("/admin/posts")
    public ResponseEntity<Page<PostResponseDTO>> getPostsAll(@RequestParam(name="postId", required = false) Long postId, @RequestParam(name="nickname", required = false) String nickname, @RequestParam(name="page", defaultValue = "1") int page, @RequestParam(name="size", defaultValue = "30") int size) {
        if( postId != null && nickname != null){
            throw new CustomException(ErrorCode.TOO_MANY_ARGUMENT);
        }
        if(nickname != null){
            return ResponseEntity.ok().body(postService.getPostsAll(nickname, page, size));
        }
        return ResponseEntity.ok().body(postService.getPostsAll(postId, page, size));
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
    public ResponseEntity<PostDetailResponseDTO> savePost(@Valid @RequestBody PostRequestDTO postDTO, @RequestParam(name="routeId") Long routeId) {
        Long postId = postService.savePost(postDTO, routeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.getPost(postId));
    }

    @Override
    @MemberRole
    @PatchMapping("/member/posts/{postId}")
    public ResponseEntity<PostDetailResponseDTO> updatePost(@Valid @RequestBody PostRequestDTO postDTO, @PathVariable(name="postId") Long postId) {
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
