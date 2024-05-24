package com.elice.tripnote.domain.post.service;

import com.elice.tripnote.domain.comment.entity.Comment;
import com.elice.tripnote.domain.comment.repository.CommentRepository;
import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import com.elice.tripnote.domain.link.likePost.repository.LikePostRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.exception.*;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final CommentService commentService;


    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;
    private final MemberRepository memberRepository;
    private final RouteRepository routeRepository;



    // 전체 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPosts(int page, int size){

        return postRepository.findByIsDeletedIsFalse(PageRequest.of(page, size, Sort.by("id").descending())).map(Post::toDTO);


    }

    // 한 유저가 쓴 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPostsByMemberId(Long memberId, int page, int size){
        memberOrElseThrowsException(memberId);

        return postRepository.findByMember_IdAndIsDeletedIsFalse(memberId, PageRequest.of(page, size, Sort.by("id").descending())).map(Post::toDTO);


    }

    // 한 유저가 좋아요 한 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getLikePostsByMemberId(Long memberId, int page, int size){
        memberOrElseThrowsException(memberId);

        return postRepository.findNotDeletedPostsByMemberIdWithLikes(memberId, PageRequest.of(page, size, Sort.by("id").descending())).map(Post::toDTO);


    }

    //  전체 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제된 게시글도 불러오며 관리자만 사용할 수 있습니다.

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPostsAll(int page, int size){

        return postRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending())).map(Post::toDTO);


    }


    // 게시글을 저장하는 메서드입니다.
    public PostResponseDTO savePost(PostRequestDTO postDTO, Long memberId, Long routeId){

        Member member = memberOrElseThrowsException(memberId);
        Route route = routeOrElseThrowsException(routeId);


        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .member(member)
                .route(route)
                .build();

//        post를 먼저 저장하고 add를 저장해야 EntityNotFoundException이 발생하지 않는다.

        postRepository.save(post);
//        route.getPost().add(post);
        member.getPosts().add(post);

        return post.toDTO();


    }



    // 게시글을 수정하는 메서드입니다.  DTO에 id가 있는지 여부는 controller단에서 검증합니다.
    public PostResponseDTO updatePost(PostRequestDTO postDTO, Long memberId){

        Post post = postOrElseThrowsException(postDTO.getId());
        if(!post.getMember().getId().equals(memberId)){
            handleNoAuthorization();
        }

        post.update(postDTO);

        return post.toDTO();
    }



    // 게시글에 좋아요를 누를 때 사용하는 메서드입니다. 이미 좋아요를 눌렀으면 좋아요를 해제합니다.
    public void createLikePost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);
        Member member = memberOrElseThrowsException(memberId);

        LikePost likePost = likePostRepository.findByPost_IdAndMember_Id(postId, memberId);

        if(likePost == null){
            likePost = LikePost.builder()
                    .member(member)
                    .post(post)
                    .build();
        }


        likePost.like();

    }



    // 게시글의 신고수를 늘리는 메서드입니다. 유저가 신고 버튼을 누를 때 사용합니다.
    public void addReportCount(Long postId){

        Post post = postOrElseThrowsException(postId);
        post.addReport();

    }

    // 게시글의 신고수를 줄이는 메서드입니다. 유저가 신고를 취소할 때 사용합니다.
    public void removeReportCount(Long postId){

        Post post = postOrElseThrowsException(postId);
        post.removeReport();

    }


    // 게시글을 삭제하는 메서드입니다. 게시글을 쓴 유저가 사용합니다.
    public void deletePost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);

        if(!post.getMember().getId().equals(memberId)){
            handleNoAuthorization();
        }
        commentService.deleteCommentsByPostId(postId);

        post.delete();

    }

    // 게시글을 삭제하는 메서드입니다. 관리자만 사용할 수 있습니다.
    public void deleteComment(Long postId){


        Post post = postOrElseThrowsException(postId);

        commentService.deleteCommentsByPostId(postId);
        post.delete();

    }







    //여기서부터는 service 내부에서만 사용할 수 있는 메서드입니다.


    // post id로 post를 불러 올 때 존재하면 post 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Post postOrElseThrowsException(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    NoSuchPostException ex = new NoSuchPostException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // comment id로 comment를 불러 올 때 존재하면 comment 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Comment commentOrElseThrowsException(Long commentId) {

        return commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    NoSuchCommentException ex = new NoSuchCommentException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // member id로 member를 불러 올 때 존재하면 member 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Member memberOrElseThrowsException(Long memberId) {

        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    NoSuchUserException ex = new NoSuchUserException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // route id로 route를 불러 올 때 존재하면 route 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Route routeOrElseThrowsException(Long routeId) {

        return routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    NoSuchRouteException ex = new NoSuchRouteException();
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    //권한이 없는 경우 NoAuthorizationException을 반환하는 메서드입니다.
    private void handleNoAuthorization(){
        NoSuchAuthorizationException ex = new NoSuchAuthorizationException();
        log.error("에러 발생: {}", ex.getMessage(), ex);
        throw ex;
    }

}
