package com.elice.tripnote.domain.link.likePost.repository;

import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    LikePost findByPostIdAndMemberId(Long postId, Long memberId);
}
