package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPost_IdAndIsDeletedIsFalse(Long postId, Pageable pageable);
    List<Comment> findByPost_IdAndIsDeletedIsFalse(Long postId);
    Page<Comment> findByMember_Id(Long userId, Pageable pageable);
    Page<Comment> findAll(Pageable pageable);
}
