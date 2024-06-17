package com.elice.tripnote.domain.post.service;


import com.elice.tripnote.domain.comment.service.CommentService;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.link.bookmark.entity.Bookmark;
import com.elice.tripnote.domain.link.bookmark.repository.BookmarkRepository;
import com.elice.tripnote.domain.link.likePost.entity.LikePost;
import com.elice.tripnote.domain.link.likePost.repository.LikePostRepository;
import com.elice.tripnote.domain.link.reportPost.entity.ReportPost;
import com.elice.tripnote.domain.link.reportPost.repository.ReportPostRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.post.entity.Post;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostRequestDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.repository.RouteRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final CommentService commentService;


    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;
    private final ReportPostRepository reportPostRepository;
    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final RouteRepository routeRepository;




    // 전체 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getPosts(PageRequestDTO pageRequestDTO){

        return postRepository.customFindNotDeletedPosts(pageRequestDTO);
    }

    public Page<PostResponseDTO> getPostsByHashtag(List<HashtagRequestDTO> hashtagRequestDTOList, PageRequestDTO pageRequestDTO){

        return postRepository.customFindByHashtagNotDeletedPosts(hashtagRequestDTOList, pageRequestDTO);
    }


    // 한 유저가 쓴 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getPostsByMemberId(PageRequestDTO pageRequestDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);
        return postRepository.customFindNotDeletedPostsByMemberId(member.getId(), pageRequestDTO);


    }

    // 한 유저가 좋아요 한 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getPostsByMemberWithLikes(PageRequestDTO pageRequestDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);

        return postRepository.customFindNotDeletedPostsWithLikesByMemberId(member.getId(), pageRequestDTO);


    }

    // 한 유저가 북마크 한 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getPostsByMemberWithMark(PageRequestDTO pageRequestDTO){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);

        return postRepository.customFindNotDeletedPostsWithMarkByMemberId(member.getId(), pageRequestDTO);


    }

    //  전체 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제된 게시글도 불러오며 관리자만 사용할 수 있습니다.

    public Page<PostResponseDTO> getPostsAll(Long postId, PageRequestDTO pageRequestDTO){

        return postRepository.customFindPosts(postId, pageRequestDTO);


    }


    public Page<PostResponseDTO> getPostsAll(String nickname, PageRequestDTO pageRequestDTO){

        return postRepository.customFindPosts(nickname, pageRequestDTO);


    }



    // 게시글을 상세 조회하는 메서드입니다. 삭제되지 않은 게시글만 볼 수 있습니다.

    public PostDetailResponseDTO getPost(Long postId){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);

        PostDetailResponseDTO postDTO = postRepository.customFindPost(postId, member.getId());
        if(postDTO == null){
            CustomException ex = new CustomException(ErrorCode.NO_POST);
            log.error("에러 발생: {}", ex.getMessage(), ex);
            throw ex;
        }

        return postDTO;


    }


    // 게시글을 저장하는 메서드입니다.
    // TO DO: presigned URL 사용 예정.
    @Transactional
    public Long savePost(PostRequestDTO postDTO, Long routeId){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);
        Route route = routeOrElseThrowsException(routeId);

        if(!postRepository.customCheckIfRouteIsAvailable(routeId, member.getId())){
            throw new CustomException(ErrorCode.NOT_VALID_ROUTE);
        }


        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .member(member)
                .route(route)
                .build();

//        post를 먼저 저장하고 add를 저장해야 EntityNotFoundException이 발생하지 않는다.

        Post newPost = postRepository.save(post);
//        route.getPost().add(post);
        member.getPosts().add(newPost);

        return newPost.getId();


    }



    // 게시글을 수정하는 메서드입니다.
    @Transactional
    public void updatePost(PostRequestDTO postDTO, Long postId){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Post post = postOrElseThrowsException(postId);
        if(!post.getMember().getEmail().equals(email)){
            handleNoAuthorization();
        }

        post.update(postDTO);

    }



    // 게시글에 좋아요를 누를 때 사용하는 메서드입니다. 이미 좋아요를 눌렀으면 좋아요를 해제합니다.
    @Transactional
    public void likePost(Long postId){

        Post post = postOrElseThrowsException(postId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = memberOrElseThrowsException(email);

        LikePost likePost = likePostRepository.findByPostIdAndMemberId(postId, member.getId());

        if(likePost == null){
            likePost = LikePost.builder()
                    .member(member)
                    .post(post)
                    .build();
            likePostRepository.save(likePost);
        }


        likePost.like();

    }

    // 게시글에 북마크를 누를 때 사용하는 메서드입니다. 이미 눌렀으면 북마크를 해제합니다.
    @Transactional
    public void markPost(Long postId){

        Post post = postOrElseThrowsException(postId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);

        Bookmark bookmark = bookmarkRepository.findByPostIdAndMemberId(postId, member.getId());

        if(bookmark == null){
            bookmark = Bookmark.builder()
                    .member(member)
                    .post(post)
                    .build();
            bookmarkRepository.save(bookmark);
        }
        bookmark.mark(null, post);

    }


    // 게시글에 신고를 누를 때 사용하는 메서드입니다. 이미 신고를 눌렀으면 신고를 해제합니다.
    @Transactional
    public void reportPost(Long postId){

        Post post = postOrElseThrowsException(postId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberOrElseThrowsException(email);

        ReportPost reportPost = reportPostRepository.findByPostIdAndMemberId(postId, member.getId());

        if(reportPost == null){
            reportPost = ReportPost.builder()
                    .member(member)
                    .post(post)
                    .build();
            reportPostRepository.save(reportPost);
        }


        reportPost.report();

    }


    // 게시글을 삭제하는 메서드입니다. 게시글을 쓴 유저가 사용합니다.
    @Transactional
    public void deletePost(Long postId){

        Post post = postOrElseThrowsException(postId);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!post.getMember().getEmail().equals(email)){
            handleNoAuthorization();
        }
        commentService.deleteCommentsByPostId(postId);

        post.delete();

        postRepository.save(post);

    }

    // 게시글을 삭제하는 메서드입니다. 관리자만 사용할 수 있습니다.
    @Transactional
    public void deletePostAdmin(Long postId){


        Post post = postOrElseThrowsException(postId);

        if(!post.isDeleted()){
            commentService.deleteCommentsByPostId(postId);
        }

        post.delete();

        postRepository.save(post);

    }







    //여기서부터는 service 내부에서만 사용할 수 있는 메서드입니다.


    // post id로 post를 불러 올 때 존재하면 post 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.

    @Transactional
    private Post postOrElseThrowsException(Long postId) {

        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_POST);
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

    // route id로 route를 불러 올 때 존재하면 route 객체를 반환하고 없으면 에러를 반환하는 메서드입니다.
    private Route routeOrElseThrowsException(Long routeId) {

        return routeRepository.findById(routeId)
                .orElseThrow(() -> {
                    CustomException ex = new CustomException(ErrorCode.NO_ROUTE);
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
