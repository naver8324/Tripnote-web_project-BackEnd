package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import org.springframework.data.domain.Page;


public interface CustomPostRepository {
    Page<PostResponseDTO> customFindNotDeletedPosts(int page, int size);
    Page<PostResponseDTO> customFindPosts(int page, int size);
    PostDetailResponseDTO customFindPost(Long postId);

    Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, int page, int size);
    Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, int page, int size);
    Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, int page, int size);
    int getLikeCount(Long integratedRouteId);

    boolean customCheckIfRouteIsAvailable(Long routeId, Long memberId);

}
