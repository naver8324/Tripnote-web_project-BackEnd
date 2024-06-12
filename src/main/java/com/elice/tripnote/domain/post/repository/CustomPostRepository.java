package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomPostRepository {
    Page<PostResponseDTO> customFindNotDeletedPosts(PageRequestDTO pageRequestDTO);
    Page<PostResponseDTO> customFindByHashtagNotDeletedPosts(List<HashtagRequestDTO> hashtagRequestDTOList, PageRequestDTO pageRequestDTO);

    Page<PostResponseDTO> customFindPosts(Long postId, PageRequestDTO pageRequestDTO);
    Page<PostResponseDTO> customFindPosts(String nickname, PageRequestDTO pageRequestDTO);
    PostDetailResponseDTO customFindPost(Long postId, Long memberId);

    Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, PageRequestDTO pageRequestDTO);
    Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, PageRequestDTO pageRequestDTO);
    Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, PageRequestDTO pageRequestDTO);

    boolean customCheckIfRouteIsAvailable(Long routeId, Long memberId);

}
