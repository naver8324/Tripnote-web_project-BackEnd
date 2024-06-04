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
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
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


    public Page<PostResponseDTO> customFindNotDeletedPosts(String order, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .where(post.isDeleted.isFalse())
                .fetchFirst();

        OrderSpecifier orderSpecifier = post.id.desc() ;

        if(order.equals("likes")){
            orderSpecifier = post.likes.desc();
        }

        List<PostResponseDTO> postResponseDTOs = query
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .where(post.isDeleted.isFalse())
                .orderBy(orderSpecifier)
                .offset(page * size)
                .limit(size)
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

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindByHashtagNotDeletedPosts(List<HashtagRequestDTO> hashtagRequestDTOList, String order, int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount = query
                .select(post.count())
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .on(hashtag.name.in(hashtagRequestDTOList.stream().map(HashtagRequestDTO::getName).collect(Collectors.toList())))
                .fetchFirst();

        OrderSpecifier orderSpecifier = post.id.desc();

        if(order.equals("likes")){
            orderSpecifier = post.likes.desc();
        }

        List<PostResponseDTO> postResponseDTOs = query
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .on(hashtag.name.in(hashtagRequestDTOList.stream().map(HashtagRequestDTO::getName).collect(Collectors.toList())))
                .where(post.isDeleted.isFalse())
                .orderBy(orderSpecifier)
                .offset(page * size)
                .limit(size)
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

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }





    public Page<PostResponseDTO> customFindPosts(int page, int size){

        page = page > 0 ? page - 1 : 0;

        long totalCount =query
                .select(post.count())
                .from(post)
                .fetchFirst();


        List<PostResponseDTO> postResponseDTOs = query
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
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

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }


    public Page<PostResponseDTO> customFindNotDeletedPostsByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

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
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);
    }

    public Page<PostResponseDTO> customFindNotDeletedPostsWithLikesByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

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
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public Page<PostResponseDTO> customFindNotDeletedPostsWithMarkByMemberId(Long memberId, int page, int size){

        page = page > 0 ? page - 1 : 0;

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
                .orderBy(post.id.desc())
                .offset(page * size)
                .limit(size)
                .fetch();

        PageRequest pageRequest = PageRequest.of(page, size);

        return new PageImpl<>(postResponseDTOs, pageRequest, totalCount);

    }


    public PostDetailResponseDTO customFindPost(Long postId){


        return query
                .from(post)
                .join(post.member, member)
                .join(post.route, route)
                .join(route.integratedRoute, integratedRoute)
                .join(integratedRoute.uuidHashtags, uuidHashtags)
                .join(uuidHashtags.hashtag, hashtag)
                .leftJoin(post.likePosts, likePost)
                .leftJoin(post.bookmarks, bookmark)
                .leftJoin(post.reportPosts, reportPost)
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
                )).get(0);



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

    public int getLikeCount(Long integratedRouteId){
        return query
                .select(post.likes.sum())
                .from(post)
                .join(route).on(post.route.id.eq(route.id)
                        .and(route.integratedRoute.id.eq(integratedRouteId)))
                .fetchOne();
    }
    
}
