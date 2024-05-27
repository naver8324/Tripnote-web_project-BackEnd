package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import org.springframework.data.domain.Page;

public interface CustomCommentRepository{
    public Page<CommentResponseDTO> customFindNotDeletedCommentsByPostId(Long postId, int page, int size);
    public Page<CommentResponseDTO> customFindComments(int page, int size);
    public Page<CommentResponseDTO> customFindCommentsByMemberId(Long memberId, int page, int size);
    public void customDeleteCommentsByPostId(Long memberId);


}
