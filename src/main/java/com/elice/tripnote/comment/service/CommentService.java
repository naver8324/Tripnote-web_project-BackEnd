package com.elice.tripnote.comment.service;

import com.elice.tripnote.comment.entity.CommentDTO;
import com.elice.tripnote.comment.entity.Comment;
import com.elice.tripnote.comment.repository.CommentRepository;
import com.elice.tripnote.post.exception.NoSuchPostException;
import com.elice.tripnote.post.entity.Post;
import com.elice.tripnote.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;



    // 게시글에서 게시글에 해당하는 댓글을 불러올 때 사용하는 메서드

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPostId(Long postId){

        Post post = postOrElseThrowsException(postId);


        return post.getComments().stream().map(Comment::toDTO).collect(Collectors.toList());


    }

    // 게시글에서 게시글에 해당하는 댓글을 페이지 형태로 불러올 때 사용하는 메서드

    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByPostId(Long postId, int page, int size){

        Post post = postOrElseThrowsException(postId);


        return commentRepository.findByPost_Id(postId, PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);


    }
//
//    public Page<ProductDTO> getProductsDTO(int page, int size) {
//        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
//                .map(product -> productMapper.toDto(product));
//    }



    // 관리자가 모든 댓글을 불러올 때 사용하는 메서드
    @Transactional(readOnly = true)
    public Page<CommentDTO> getComments(int page, int size){

        return commentRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);


    }

    // 관리자가 한 유저의 모든 댓글을 불러올 때 사용하는 메서드
//    public List<CommentDTO> getCommentsByUserId(Long userId){
//
//        user user = userRepository.findById(userId).orElse(null);
//        if(post == null){
//            NoSuchUserException ex = new NoSuchUserException();
//
//            log.error("에러 발생: {}", ex.getMessage(), ex);
//            throw ex;
//        }
//
//        return commentRepository.findByUser_Id(postId, PageRequest.of(page, size, Sort.by("id").descending())).map(Comment::toDTO);
//
//
//
//    }


    // 댓글을 저장하는 메서드입니다.
    public CommentDTO saveComment(CommentDTO commentDTO, Long postId){

        Post post = postOrElseThrowsException(postId);

        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .post(post)
                .build();

        // comment를 먼저 저장하고 add를 저장해야 EntityNotFoundException이 발생하지 않는다.

        commentRepository.save(comment);
        post.getComments().add(comment);

        return comment.toDTO();


    }

    // post id로 post를 불러 올 때 존재하면 post 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.

    @Transactional(readOnly = true)
    private Post postOrElseThrowsException(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    NoSuchPostException ex = new NoSuchPostException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }


}
