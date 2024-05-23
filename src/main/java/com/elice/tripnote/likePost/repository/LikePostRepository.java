package com.elice.tripnote.likePost.repository;

import com.elice.tripnote.likePost.entity.LikePost;
import com.elice.tripnote.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    LikePost findByPost_IdAndMember_Id(Long postId, Long memberId);


}
