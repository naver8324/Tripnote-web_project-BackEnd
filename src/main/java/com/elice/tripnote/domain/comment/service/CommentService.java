package com.elice.tripnote.domain.comment.service;

import com.elice.tripnote.domain.comment.entity.CommentRequestDTO;
import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.repository.CommentRepository;
import com.elice.tripnote.domain.link.reportComment.entity.ReportComment;
import com.elice.tripnote.domain.link.reportComment.repository.ReportCommentRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.post.repository.PostRepository;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReportCommentRepository reportCommentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    // 게시글에서 게시글에 해당하는 댓글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 댓글만 불러옵니다.




    private CommentResponseDTO getComment(Long commentId){

        return commentRepository.customFindNotDeletedComment(commentId);


    }

    public Page<CommentResponseDTO> getCommentsByPostId(Long postId, PageRequestDTO pageRequestDTO){


        Post post = postOrElseThrowsException(postId);


        return commentRepository.customFindNotDeletedCommentsByPostId(postId, pageRequestDTO);


    }




    // 관리자가 모든 댓글을 불러올 때 사용하는 메서드. 삭제된 댓글도 불러옵니다.
    public Page<CommentResponseDTO> getCommentsAll(Long commentId, PageRequestDTO pageRequestDTO){



        return commentRepository.customFindComments(commentId, pageRequestDTO);


    }

    public Page<CommentResponseDTO> getCommentsAll(String nickname, PageRequestDTO pageRequestDTO){



        return commentRepository.customFindComments(nickname, pageRequestDTO);


    }




    // 댓글을 저장하는 메서드입니다.

    @Transactional
    public CommentResponseDTO saveComment(CommentRequestDTO commentDTO, Long postId){

        Post post = postOrElseThrowsException(postId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);


        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .post(post)
                .member(member)
                .build();

        // comment를 먼저 저장하고 add를 저장해야 EntityNotFoundException이 발생하지 않는다.

        commentRepository.save(comment);
        post.getComments().add(comment);
        member.getComments().add(comment);


        return getComment(comment.getId());


    }


    // 댓글을 수정하는 메서드입니다.

    @Transactional
    public CommentResponseDTO updateComment(CommentRequestDTO commentDTO, Long commentId){

        Comment comment = commentOrElseThrowsException(commentId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!comment.getMember().getEmail().equals(email)){
            handleNoAuthorization();
        }

        comment.update(commentDTO);
        commentRepository.flush();

        return getComment(comment.getId());

    }

    // 댓글에 신고를 누를 때 사용하는 메서드입니다. 이미 신고를 눌렀으면 신고를 해제합니다.
    @Transactional
    public void reportComment(Long commentId){

        Comment comment = commentOrElseThrowsException(commentId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberOrElseThrowsException(email);

        ReportComment reportComment = reportCommentRepository.findByCommentIdAndMemberId(commentId, member.getId());

        if(reportComment == null){
            reportComment = ReportComment.builder()
                    .member(member)
                    .comment(comment)
                    .build();
            reportComment = reportCommentRepository.save(reportComment);

        }



        reportComment.report();

    }



    // 댓글을 삭제하는 메서드입니다. 댓글을 쓴 유저가 사용합니다.
    @Transactional
    public void deleteComment(Long commentId){

        Comment comment = commentOrElseThrowsException(commentId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!comment.getMember().getEmail().equals(email)){
            handleNoAuthorization();
        }

        comment.delete();
        commentRepository.save(comment);

    }

    // 댓글을 삭제하는 메서드입니다. 관리자만 사용할 수 있습니다.

    @Transactional
    public void deleteCommentAdmin(Long commentId){


        Comment comment = commentOrElseThrowsException(commentId);
        comment.delete();

        commentRepository.save(comment);

    }

    // 게시글을 삭제할 때 게시글에 달린 댓글들을 삭제하는 메서드입니다.

    @Transactional
    public void deleteCommentsByPostId(Long postId) {

        commentRepository.customDeleteCommentsByPostId(postId);
    }









    //여기서부터는 service 내부에서만 사용할 수 있는 메서드입니다.


    // post id로 post를 불러 올 때 존재하면 post 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Post postOrElseThrowsException(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_POST);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // comment id로 comment를 불러 올 때 존재하면 comment 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Comment commentOrElseThrowsException(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_COMMENT);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // member id로 member를 불러 올 때 존재하면 member 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Member memberOrElseThrowsException(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_USER);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // member email로 member를 불러 올 때 존재하면 member 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Member memberOrElseThrowsException(String email) {

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_USER);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    //권한이 없는 경우 NoAuthorizationException을 반환하는 메서드입니다.
    private void handleNoAuthorization(){
        CustomException ex = new CustomException(ErrorCode.UNAUTHORIZED);
        log.error("에러 발생: {}", ex.getMessage(), ex);
        throw ex;
    }


}
