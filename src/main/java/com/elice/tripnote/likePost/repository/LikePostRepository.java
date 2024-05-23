package com.elice.tripnote.likePost.repository;

import com.elice.tripnote.likePost.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {


}
