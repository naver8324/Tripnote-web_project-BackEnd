package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomPostRepository {
    Page<PostResponseDTO> customFindNotDeletedPosts(String order, int page, int size);
    Page<PostResponseDTO> customFindByHashtagNotDeletedPosts(List<HashtagRequestDTO> hashtagRequestDTOList, String order, int page, int size);

    Page<PostResponseDTO> customFindPosts(Long memberId, int page, int size);
    PostDetailResponseDTO customFindPost(Long postId, Long memberId);

    Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, int page, int size);
    Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, int page, int size);
    Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, int page, int size);
    int getLikeCount(Long integratedRouteId);

    boolean customCheckIfRouteIsAvailable(Long routeId, Long memberId);

}
