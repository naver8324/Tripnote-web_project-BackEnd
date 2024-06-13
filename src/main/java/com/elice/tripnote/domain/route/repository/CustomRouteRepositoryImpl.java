package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.post.entity.QPost;
import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.entity.QSpot;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Repository
@RequiredArgsConstructor
public class CustomRouteRepositoryImpl implements CustomRouteRepository {
    private final JPAQueryFactory query;
    private final QRoute route = new QRoute("r");
    private final QRouteSpot routeSpot = new QRouteSpot("rs");
    private final QSpot spot = new QSpot("s");
    private final QLikePost likePost = new QLikePost("lp");
    private final QBookmark bookmark = new QBookmark("b");
    private final QIntegratedRoute integratedRoute = new QIntegratedRoute("ir");
    private final QPost post = new QPost("p");
    private final QHashtag hashtag = new QHashtag("h");
    private final QLikeBookmarkPeriod lbp = new QLikeBookmarkPeriod("lbp");


//    public List<RouteIdNameResponseDTO> findLikedRoutesByMemberId(Long memberId) {
//        return query
//                .select(Projections.constructor(RouteIdNameResponseDTO.class,
//                        route.id,
//                        route.name
//                ))
//                .from(route)
//                .join(likePost).on(likePost.route.id.eq(route.id))
//                .where(likePost.member.id.eq(memberId))
//                .fetch();
//    }

    public Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, PageRequestDTO pageRequestDTO, boolean isBookmark) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
//        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        long total = 0;
        List<RouteIdNameResponseDTO> routes;
        if (isBookmark) {
            routes = query
                    .select(Projections.constructor(RouteIdNameResponseDTO.class,
                            post.id,
                            route.id,
                            route.name
                    ))
                    .from(route)
                    .join(bookmark).on(bookmark.route.id.eq(route.id))
                    .leftJoin(post).on(post.route.id.eq(route.id))
                    .where(bookmark.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .offset(pageRequest.getOffset())
                    .limit(pageRequest.getPageSize())
                    .orderBy(route.id.asc())
                    .fetch();

            total = query
                    .select(route.count())
                    .from(route)
                    .join(bookmark).on(bookmark.route.id.eq(route.id))
                    .leftJoin(post).on(post.route.id.eq(route.id))
                    .where(bookmark.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .fetchOne();

        } else {
            routes = query
                    .select(Projections.constructor(RouteIdNameResponseDTO.class,
                            post.id,
                            route.id,
                            route.name
                    ))
                    .from(route)
                    .leftJoin(post).on(post.route.id.eq(route.id))
                    .where(route.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .offset(pageRequest.getOffset())
                    .limit(pageRequest.getPageSize())
                    .orderBy(route.id.asc())
                    .fetch();

            total = query
                    .select(route.count())
                    .from(route)
                    .where(route.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .fetchOne();
        }

        // 그 중 경로 id만 리스트로 추출
        List<Long> routeIds = routes.stream()
                .map(RouteIdNameResponseDTO::getRouteId)
                .collect(Collectors.toList());

        List<SpotRouteIdDTO> spots = query
                .select(Projections.constructor(SpotRouteIdDTO.class,
                        routeSpot.route.id,
                        spot.id,
                        spot.location,
                        spot.imageUrl,
                        spot.region,
                        spot.address,
                        spot.lat,
                        spot.lng
                ))
                .from(routeSpot)
                .join(spot).on(spot.id.eq(routeSpot.spot.id))
                .where(routeSpot.route.id.in(routeIds))
                // 일단 route id 순서대로, 그 안에서는 sequence 순서대로
                .orderBy(routeSpot.route.id.asc(), routeSpot.sequence.asc())
                .fetch();

        Map<Long, List<SpotRouteIdDTO>> routeIdToSpotsMap = spots.stream()
                .collect(Collectors.groupingBy(SpotRouteIdDTO::getRouteId));

        List<RouteDetailResponseDTO> routeDetails = routes.stream()
                .map(routeDto -> new RouteDetailResponseDTO(
                        routeDto.getRouteId(),
                        routeDto.getPostId(),
                        routeDto.getName(),
                        routeIdToSpotsMap.getOrDefault(routeDto.getRouteId(), List.of())
                                .stream()
                                .map(spotDto -> new Spot(
                                        spotDto.getId(),
                                        spotDto.getLocation(),
                                        spotDto.getImageUrl(),
                                        spotDto.getRegion(),
                                        spotDto.getAddress(),
                                        spotDto.getLat(),
                                        spotDto.getLng()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());


        return new PageImpl<>(routeDetails, pageRequest, total);
    }


    public int getIntegratedRouteLikeCounts(Long integratedRouteId) {
        /*
        select count(lp.id)
        from route r
        join integrated ir on r.integrated_id=ir.id
        join like_post lp on r.id=lp.route_id
        where ir.id=:integratedRouteId
        group by ir.id

         */
        Integer likeCount = query
                .select(likePost.id.count().intValue())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .join(likePost).on(likePost.route.id.eq(route.id))
                .where(integratedRoute.id.eq(integratedRouteId))
                .groupBy(integratedRoute.id)
                .fetchOne();

        return likeCount != null ? likeCount : 0;
    }

//    public Map<Long, Integer> getIntegratedRouteLikeCounts(List<Long> integratedIds) {
//        //특정 통합 경로의 좋아요 개수 구하기
//        List<Tuple> results = query
//                .select(integratedRoute.id, likePost.id.count().intValue())
//                .from(route)
//                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
//                .join(likePost).on(likePost.route.id.eq(route.id))
//                .where(integratedRoute.id.in(integratedIds))
//                .groupBy(integratedRoute.id)
//                .fetch();
//
//        return results.stream()
//                .collect(Collectors.toMap(
//                        tuple -> tuple.get(integratedRoute.id),
//                        tuple -> tuple.get(likePost.id.count().intValue())
//                ));
//    }

    public Route getMinRouteByIntegratedId(Long integratedId) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId));

        return query
                .select(route)
                .from(route)
                .where(route.id.eq(minRouteId))
                .fetchOne();
    }

//    public Long findPostIdByIntegratedRouteId(Long integratedId) {
//        JPQLQuery<Long> minRouteId = JPAExpressions
//                .select(route.id.min())
//                .from(route)
//                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
//                .where(integratedRoute.id.eq(integratedId)
//                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)));
//
//        return query
//                .select(post.id)
//                .from(post)
//                .join(route).on(post.route.id.eq(route.id))
//                .where(route.id.eq(minRouteId))
//                .fetchOne();
//
//    }

//    public Map<Long, Long> findPostIdsByIntegratedRouteIds(List<Long> integratedIds) {
//        List<Tuple> results = query
//                .select(post.route.integratedRoute.id, post.id, likePost.id.count().as("likeCount"))
//                .from(post)
//                .join(likePost).on(likePost.post.id.eq(post.id))
//                .where(post.route.integratedRoute.id.in(integratedIds)
//                        .and(post.route.routeStatus.eq(RouteStatus.PUBLIC)))
//                .groupBy(post.route.integratedRoute.id, post.id)
//                .orderBy(post.route.integratedRoute.id.asc(), likePost.id.count().desc())
//                .fetch();
//
//        Map<Long, Long> resultMap = new LinkedHashMap<>(); // 순서가 유지되도록 LinkedHashMap 사용
//
//        results.forEach(tuple -> resultMap.put(tuple.get(post.route.integratedRoute.id), tuple.get(post.id)));
//
//        return resultMap;
//    }

    public boolean findHashtagIdIdCity(Long hashtagId) {

        return query
                .select(hashtag.isCity)
                .from(hashtag)
                .where(hashtag.id.eq(hashtagId))
                .fetchOne();
    }

    public List<RecommendedRouteResponseDTO> getRecommendedRoutes(List<Long> integratedRouteIds, Long memberId, boolean isMember) {

        List<Tuple> results = query
                .select(
                        integratedRoute.id,
                        post.id,
                        post.id.count().as("postLikes"),
                        route.id.min().as("routeId"),
                        spot.id,
                        spot.location,
                        spot.imageUrl,
                        spot.region,
                        spot.address,
                        spot.lat,
                        spot.lng,
                        Expressions.as(
                                JPAExpressions.select(likePost.id.count())
                                        .from(likePost)
                                        .where(likePost.route.id.eq(route.id.min())),
                                "likes"
                        ),
                        Expressions.as(
                                isMember ? JPAExpressions.select(likePost.id.count())
                                        .from(likePost)
                                        .where(likePost.member.id.eq(memberId)
                                                .and(likePost.route.id.eq(route.id.min())))
                                        : Expressions.constant(0L),
                                "likedAt"
                        ),
                        Expressions.as(
                                isMember ? JPAExpressions.select(bookmark.id.count())
                                        .from(bookmark)
                                        .where(bookmark.member.id.eq(memberId)
                                                .and(bookmark.route.id.eq(route.id.min())))
                                        : Expressions.constant(0L),
                                "markedAt"
                        )
                )
                .from(integratedRoute)
                .join(route).on(route.integratedRoute.id.eq(integratedRoute.id))
                .leftJoin(post).on(post.route.id.eq(route.id))  // join -> leftjoin으로 변경
                .leftJoin(post.likePosts, likePost)
                .leftJoin(routeSpot).on(routeSpot.route.id.eq(route.id))
                .leftJoin(spot).on(routeSpot.spot.id.eq(spot.id))
                .where(integratedRoute.id.in(integratedRouteIds))
                .groupBy(integratedRoute.id, post.id, spot.id, spot.location, spot.imageUrl, spot.region, spot.address, spot.lat, spot.lng)
                .fetch();


        // 결과를 처리하여 DTO 리스트 생성
        Map<Long, RecommendedRouteResponseDTO> resultMap = results.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(integratedRoute.id),
                        Collectors.collectingAndThen(Collectors.toList(), tuples -> {
                            int likes = Math.toIntExact((Long) tuples.get(0).get(Expressions.path(Long.class, "likes")));
                            boolean likedAt = tuples.get(0).get(Expressions.path(Long.class, "likedAt")) > 0;
                            boolean markedAt = tuples.get(0).get(Expressions.path(Long.class, "markedAt")) > 0;

                            List<Spot> spots = tuples.stream()
                                    .map(tuple -> new Spot(tuple.get(spot.id),
                                            tuple.get(spot.location),
                                            tuple.get(spot.imageUrl),
                                            tuple.get(spot.region),
                                            tuple.get(spot.address),
                                            tuple.get(spot.lat),
                                            tuple.get(spot.lng)))
                                    .collect(Collectors.toList());

                            return new RecommendedRouteResponseDTO(
                                    tuples.get(0).get(integratedRoute.id),
                                    tuples.get(0).get(post.id),
                                    spots,
                                    likes,
                                    likedAt,
                                    markedAt
                            );
                        })
                ));

        // integratedRouteIds 순서에 맞춰 정렬된 List 생성
        return integratedRouteIds.stream()
                .filter(resultMap::containsKey)  // 매핑된 값만 필터링
                .map(resultMap::get)  // 순서대로 매핑된 값 가져오기
                .collect(Collectors.toList());
    }

    public List<Long> findIntegratedRouteIdsBySpotsAndLikes(List<Long> spots) {
        JPQLQuery<Long> integratedRouteQuery = query
                .selectDistinct(route.integratedRoute.id)
                .from(route)
                .join(routeSpot).on(routeSpot.route.id.eq(route.id))
                .where(routeSpot.spot.id.in(spots))
                .groupBy(route.id, route.integratedRoute.id)
                .having(routeSpot.spot.id.countDistinct().eq((long) spots.size()));

//        JPQLQuery<LocalDateTime> maxStartAtSubquery = JPAExpressions.select(lbp.startAt.max())
//                .from(lbp)
//                .where(lbp.integratedRoute.id.eq(integratedRoute.id))
//                .groupBy(lbp.integratedRoute.id);


        return query
                .select(integratedRoute.id)
                .from(integratedRoute)
                .join(lbp).on(integratedRoute.id.eq(lbp.integratedRoute.id))
                .where(
                        integratedRoute.id.in(integratedRouteQuery)
//                                .and(lbp.startAt.eq(maxStartAtSubquery))
                                .and(integratedRoute.routeStatus.eq(RouteStatus.PUBLIC))
                )
                .groupBy(integratedRoute.id)
                .orderBy(lbp.likes.sum().desc())
                .limit(5)
                .fetch();
    }

    public boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId, boolean isLike) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
//                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(route.integratedRoute.id.eq(integratedId));

        if (isLike) {
            return query
                    .select(likePost.id)
                    .from(likePost)
//                .join(route).on(likePost.route.id.eq(route.id))
                    .where(likePost.route.id.eq(minRouteId).and(likePost.member.id.eq(memberId)))
                    .fetchCount() > 0;
        } else {
            return query
                    .select(bookmark.id)
                    .from(bookmark)
//                    .join(route).on(bookmark.route.id.eq(route.id))
                    .where(bookmark.route.id.eq(minRouteId).and(bookmark.member.id.eq(memberId)))
                    .fetchCount() > 0;
        }


    }

    public void deleteByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId, boolean isLike) {

        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId));

        if (isLike) {
            query.delete(likePost)
                    .where(likePost.route.id.eq(minRouteId)
                            .and(likePost.member.id.eq(memberId)))
                    .execute();
        } else {
            query.delete(bookmark)
                    .where(bookmark.route.id.eq(minRouteId)
                            .and(bookmark.member.id.eq(memberId)))
                    .execute();
        }
    }


}
