package com.elice.tripnote.domain.post.service;


import com.elice.tripnote.domain.comment.service.CommentService;
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
import com.elice.tripnote.domain.post.exception.*;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.repository.RouteRepository;
import com.elice.tripnote.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<PostResponseDTO> getPosts(int page, int size){

        return postRepository.customFindNotDeletedPosts(page, size);
    }

    // 한 유저가 쓴 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getPostsByMemberId(Long memberId, int page, int size){

        memberOrElseThrowsException(memberId);
        return postRepository.customFindNotDeletedPostsByMemberId(memberId, page, size);


    }

    // 한 유저가 좋아요 한 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제되지 않은 게시글만 불러옵니다.

    public Page<PostResponseDTO> getLikePostsByMemberId(Long memberId, int page, int size){
        memberOrElseThrowsException(memberId);

        return postRepository.customFindNotDeletedPostsWithLikesByMemberId(memberId, page, size);


    }

    //  전체 게시글을 페이지 형태로 불러올 때 사용하는 메서드. 삭제된 게시글도 불러오며 관리자만 사용할 수 있습니다.

    public Page<PostResponseDTO> getPostsAll(int page, int size){

        return postRepository.customFindPosts(page, size);


    }



    // 게시글을 상세 조회하는 메서드입니다. 삭제되지 않은 게시글만 볼 수 있습니다.

    public PostDetailResponseDTO getPost(Long postId, Long memberId){

        memberOrElseThrowsException(memberId);

        PostDetailResponseDTO postDTO = postRepository.customFindPost(postId);
        if(postDTO == null){
            NoSuchPostException ex = new NoSuchPostException();
            log.error("에러 발생: {}", ex.getMessage(), ex);
            throw ex;
        }

        return postDTO;


    }


    // 게시글을 저장하는 메서드입니다.
    @Transactional
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
    @Transactional
    public PostResponseDTO updatePost(PostRequestDTO postDTO, Long memberId){

        Post post = postOrElseThrowsException(postDTO.getId());
        if(!post.getMember().getId().equals(memberId)){
            handleNoAuthorization();
        }

        post.update(postDTO);

        return post.toDTO();
    }



    // 게시글에 좋아요를 누를 때 사용하는 메서드입니다. 이미 좋아요를 눌렀으면 좋아요를 해제합니다.
    @Transactional
    public void LikePost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);
        Member member = memberOrElseThrowsException(memberId);

        LikePost likePost = likePostRepository.findByPostIdAndMemberId(postId, memberId);

        if(likePost == null){
            likePost = LikePost.builder()
                    .member(member)
                    .post(post)
                    .build();
        }


        likePost.like();

    }

    // 게시글에 북마크를 누를 때 사용하는 메서드입니다. 이미 눌렀으면 신고를 해제합니다.
    @Transactional
    public void markPost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);
        Member member = memberOrElseThrowsException(memberId);

        Bookmark bookmark = bookmarkRepository.findByPostIdAndMemberId(postId, memberId);

        if(bookmark == null){
            bookmark = Bookmark.builder()
                    .member(member)
                    .post(post)
                    .build();
        }
        bookmark.mark(null, post);

    }


    // 게시글에 신고를 누를 때 사용하는 메서드입니다. 이미 신고를 눌렀으면 신고를 해제합니다.
    @Transactional
    public void reportPost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);
        Member member = memberOrElseThrowsException(memberId);

        ReportPost reportPost = reportPostRepository.findByPostIdAndMemberId(postId, memberId);

        if(reportPost == null){
            reportPost = ReportPost.builder()
                    .member(member)
                    .post(post)
                    .build();
        }


        reportPost.report();

    }


    // 게시글을 삭제하는 메서드입니다. 게시글을 쓴 유저가 사용합니다.
    @Transactional
    public void deletePost(Long postId, Long memberId){

        Post post = postOrElseThrowsException(postId);

        if(!post.getMember().getId().equals(memberId)){
            handleNoAuthorization();
        }
        commentService.deleteCommentsByPostId(postId);

        post.delete();

    }

    // 게시글을 삭제하는 메서드입니다. 관리자만 사용할 수 있습니다.
    @Transactional
    public void deletePost(Long postId){


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
