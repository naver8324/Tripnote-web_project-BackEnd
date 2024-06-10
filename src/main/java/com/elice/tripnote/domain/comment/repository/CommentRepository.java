package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository{

}
