package com.elice.tripnote.domain.post.repository;

import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.reportPost.entity.QReportPost;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
import com.elice.tripnote.domain.member.entity.QMember;
import com.elice.tripnote.domain.post.entity.PostDetailResponseDTO;
import com.elice.tripnote.domain.post.entity.PostResponseDTO;
import com.elice.tripnote.domain.post.entity.QPost;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory query;


    private final QMember member = QMember.member;


    private final QPost post = QPost.post;
    private final QRoute route = QRoute.route;
    private final QIntegratedRoute integratedRoute = QIntegratedRoute.integratedRoute;
    private final QUUIDHashtag uuidHashtags = QUUIDHashtag.uUIDHashtag;
    private final QHashtag hashtag = QHashtag.hashtag;

    private final QLikePost likePost = QLikePost.likePost;
    private final QReportPost reportPost = QReportPost.reportPost;

    private final QBookmark bookmark = QBookmark.bookmark;

    public Page<PostResponseDTO> customFindNotDeletedPosts(PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.isDeleted.isFalse())
                .fetchFirst();

        List<Long> postIds = query
                .select(post.id)
                .from(post)
                .where(post.isDeleted.isFalse())
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        List<PostResponseDTO> postResponseDTOs = query
                .from(post)
                .join(post.member, member)
                .on(post.id.in(postIds))
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .orderBy(orderSpecifier)
                .transform(groupBy(post.id).list(Projections.constructor(PostResponseDTO.class,
                                post.id,
                                post.title,
                                post.content,
                                post.isDeleted,
                                post.createdAt,
                                member.nickname,
                                list(Projections.constructor(HashtagResponseDTO.class,
                                        hashtag.id,
                                        hashtag.name,
                                        hashtag.isCity)
                                )
                        )
                ));


//
//        List<PostResponseDTO> postResponseDTOs = query
//                .from(post)
//                .join(post.member, member)
//                .join(post.route, route)
//                .join(route.integratedRoute, integratedRoute)
//                .join(integratedRoute.uuidHashtags, uuidHashtags)
//                .join(uuidHashtags.hashtag, hashtag)
//                .where(post.isDeleted.isFalse())
//                .orderBy(orderSpecifier)
//                .offset(page * size)
//                .limit(size)
//                .transform(groupBy(post.id).list(Projections.constructor(PostResponseDTO.class,
//                                post.id,
//                                post.title,
//                                post.content,
//                                post.isDeleted,
//                                post.createdAt,
//                                member.nickname,
//                                list(Projections.constructor(HashtagResponseDTO.class,
//                                        hashtag.id,
//                                        hashtag.name,
//                                        hashtag.isCity)
//                                )
//                        )
//                ));

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindByHashtagNotDeletedPosts(List<HashtagRequestDTO> hashtagRequestDTOList, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        List<String> hashtagNames = hashtagRequestDTOList.stream().map(HashtagRequestDTO::getName).toList();
        log.info("hashtagNames ={}", hashtagNames);

        JPQLQuery<Long> subQuery = JPAExpressions
                .select(post.id)
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .on(hashtag.name.in(hashtagNames))
                .where(post.isDeleted.isFalse())
                .groupBy(post.id)
                .having(hashtag.count().eq((long) hashtagRequestDTOList.size()));

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.id.in(subQuery))
                .fetchFirst();

        List<Long> postIds =query
                .select(post.id)
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .on(hashtag.name.in(hashtagNames))
                .where(post.isDeleted.isFalse())
                .orderBy(orderSpecifier)
                .orderBy(post.createdAt.desc())
                .groupBy(post.id)
                .having(hashtag.count().eq((long) hashtagRequestDTOList.size()))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();


        List<PostResponseDTO> postResponseDTOs = query
                .from(post)
                .join(post.member, member)
                .on(post.id.in(postIds))
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .orderBy(orderSpecifier)
                .orderBy(post.createdAt.desc())
                .transform(groupBy(post.id).list(Projections.constructor(PostResponseDTO.class,
                                post.id,
                                post.title,
                                post.content,
                                post.isDeleted,
                                post.createdAt,
                                member.nickname,
                                list(Projections.constructor(HashtagResponseDTO.class,
                                        hashtag.id,
                                        hashtag.name,
                                        hashtag.isCity)
                                )
                        )
                ));

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }





    public Page<PostResponseDTO> customFindPosts(Long postId, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        Long memberId = postId != null? query.select(member.id).from(member).join(member.posts, post).where(post.id.eq(postId)).fetchFirst() : null;

        long totalCount =query
                .select(post.count())
                .from(post)
                .join(post.member, member)
                .where(memberId != null ? member.id.eq(memberId) : null)
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted,
                        post.createdAt,
                        member.nickname
                ))
                .from(post)
                .join(post.member, member)
                .where(memberId != null ? member.id.eq(memberId) : null)
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindPosts(String nickname, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        Long memberId = query.select(member.id).from(member).where(member.nickname.eq(nickname)).fetchFirst();

        if(memberId == null){
            memberId = -1L;
        }

        long totalCount =query
                .select(post.count())
                .from(post)
                .join(post.member, member)
                .where(member.id.eq(memberId))
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted,
                        post.createdAt,
                        member.nickname
                ))
                .from(post)
                .join(post.member, member)
                .where(member.id.eq(memberId))
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }




    public Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.member.id.eq(memberId)
                        .and(post.isDeleted.isFalse()))
                .fetchFirst();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted,
                        post.createdAt,
                        member.nickname
                        ))
                .from(post)
                .join(post.member, member)
                .on(post.member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        long totalCount = query
                .select(post.count())
                .from(post)
                .join(post.likePosts, likePost)
                .on(likePost.likedAt.isNotNull())
                .join(likePost.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .fetchFirst();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted,
                        post.createdAt,
                        post.member.nickname
                ))
                .from(post)
                .join(post.likePosts, likePost)
                .on(likePost.likedAt.isNotNull())
                .join(likePost.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        long totalCount = query
                .select(post.count())
                .from(post)
                .join(post.bookmarks, bookmark)
                .on(bookmark.markedAt.isNotNull())
                .join(bookmark.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .fetch().size();

        List<PostResponseDTO> postResponseDTOs = query
                .select(Projections.constructor(PostResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.isDeleted,
                        post.createdAt,
                        post.member.nickname
                ))
                .from(post)
                .join(post.bookmarks, bookmark)
                .on(bookmark.markedAt.isNotNull())
                .join(bookmark.member, member)
                .on(member.id.eq(memberId))
                .where(post.isDeleted.isFalse())
                .orderBy(post.createdAt.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public PostDetailResponseDTO customFindPost(Long postId, Long memberId){


        List<PostDetailResponseDTO> result =query
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .leftJoin(post.likePosts, likePost)
                .on(likePost.member.id.eq(memberId))
                .leftJoin(post.bookmarks, bookmark)
                .on(bookmark.member.id.eq(memberId))
                .leftJoin(post.reportPosts, reportPost)
                .on(reportPost.member.id.eq(memberId))
                .where(post.id.eq(postId)
                        .and(post.isDeleted.isFalse()))
                .transform(groupBy(post.id).list(Projections.constructor(PostDetailResponseDTO.class,
                        post.id,
                        post.title,
                        post.content,
                        post.likes,
                        post.report,
                        post.isDeleted,
                        post.createdAt,
                        likePost.likedAt,
                        bookmark.markedAt,
                        reportPost.reportedAt,
                        route.id,
                        member.nickname,
                                list(Projections.constructor(HashtagResponseDTO.class,
                                        hashtag.id,
                                        hashtag.name,
                                        hashtag.isCity)
                                )
                        )
                ));

        if(result.isEmpty()){
            return null;
        }
        return result.get(0);

    }


    public boolean customCheckIfRouteIsAvailable(Long routeId, Long memberId){

        return query
                .select(Expressions.ONE)
                .from(route)
                .leftJoin(post)
                .on(post.route.id.eq(route.id))
                .where( route.id.eq(routeId)
                        .and(post.id.isNull())
                        .and(route.member.id.eq(memberId))
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                .fetchFirst() != null;

    }


    private OrderSpecifier<?> getOrderSpecifier(String order, boolean asc) {   //정렬 방식 정하기

        if ("id".equals(order)) {   //만약 sort가 "id"인 경우
            return post.id.desc();   //desc가 true일 때 : hashtag.id.desc() 를 orderSpecifier<?>에 저장
        } else if ("likes".equals(order)) {
            return post.likes.desc();
        }
        // 기본값 설정
        return post.id.desc();  //order에 값이 없을 경우 id를 기준으로 정렬
    }
}
