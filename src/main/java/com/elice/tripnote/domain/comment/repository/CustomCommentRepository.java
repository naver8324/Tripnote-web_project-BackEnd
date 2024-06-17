package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface CustomCommentRepository{

    public CommentResponseDTO customFindNotDeletedComment(Long commentId);
    public Page<CommentResponseDTO> customFindNotDeletedCommentsByPostId(Long postId, PageRequestDTO pageRequestDTO);
    public Page<CommentResponseDTO> customFindComments(Long commentId, PageRequestDTO pageRequestDTO);
    public Page<CommentResponseDTO> customFindComments(String nickname, PageRequestDTO pageRequestDTO);
    public void customDeleteCommentsByPostId(Long memberId);


}
