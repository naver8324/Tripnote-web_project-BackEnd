package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.post.entity.QPost;
import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.entity.QSpot;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<Long> findIntegratedRouteIdsBySpots(List<Long> spots) {
        /*
        select distinct r.integrated_route_id
        from route r
        join route_spot rs on rs.route_id = r.id
        where rs.spot_id in :spots  -- spot_id가 spots 중에 하나일 때만 통과
        group by r.id, r.integrated_route_id    -- 같은 통합 경로 id 그룹 안에 각 경로 id끼리 그룹
        having count(distinct rs.spot_id) = :spots.size()   -- route id 기준으로 묶었을 때, 겹치지 않는 spot_id가 spots.size개인 row만
                                                               (spot_id가 spots 중에 하나일 때만 통과이므로 spot_id가 spots의 크기라면 모든 spots가 있음)
         */
        return query
                .selectDistinct(route.integratedRoute.id)
                .from(route)
                .join(routeSpot).on(routeSpot.route.id.eq(route.id))
                .where(routeSpot.spot.id.in(spots))
                .groupBy(route.id, route.integratedRoute.id)
                .having(routeSpot.spot.id.countDistinct().eq((long) spots.size()))
                .fetch();
    }

    public List<RouteIdNameResponseDTO> findLikedRoutesByMemberId(Long memberId) {
        return query
                .select(Projections.constructor(RouteIdNameResponseDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .join(likePost).on(likePost.route.id.eq(route.id))
                .where(likePost.member.id.eq(memberId))
                .fetch();
    }

//    public Page<RouteIdNameDTO> findMarkedRoutesByMemberId(Long memberId, Pageable pageable) {
//
//        return query
//                .select(Projections.constructor(RouteIdNameDTO.class,
//                        route.id,
//                        route.name
//                ))
//                .from(route)
//                .join(bookmark).on(bookmark.route.id.eq(route.id))
//                .where(bookmark.member.id.eq(memberId))
//                .orderBy(route.id.asc())
//                .fetch();
//
//
//    }

//    public Page<RouteIdNameResponseDTO> findMarkedRoutesByMemberId(Long memberId, Pageable pageable) {
//
//        QueryResults<RouteIdNameResponseDTO> queryResults = query
//                .select(Projections.constructor(RouteIdNameResponseDTO.class,
//                        route.id,
//                        route.name
//                ))
//                .from(route)
//                .join(bookmark).on(bookmark.route.id.eq(route.id))
//                .where(bookmark.member.id.eq(memberId)
//                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
//                .orderBy(route.id.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(route.id.desc())
//                .fetchResults();
//
//        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
//    }

    public Page<RouteDetailResponseDTO> findRouteDetailsByMemberId(Long memberId, Pageable pageable, boolean isBookmark) {

        List<RouteIdNameResponseDTO> routes;
        if(isBookmark){
            routes = query
                    .select(Projections.constructor(RouteIdNameResponseDTO.class,
                            route.id,
                            route.name
                    ))
                    .from(route)
                    .join(bookmark).on(bookmark.route.id.eq(route.id))
                    .where(bookmark.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(route.id.asc())
                    .fetch();
        } else{
            routes = query
                    .select(Projections.constructor(RouteIdNameResponseDTO.class,
                            route.id,
                            route.name
                    ))
                    .from(route)
                    .where(route.member.id.eq(memberId)
                            .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(route.id.asc())
                    .fetch();
        }
        //먼저 멤버 id로 자신이 쓴 경로 아이디 알아내기
//        List<RouteIdNameResponseDTO> routes = query
//                .select(Projections.constructor(RouteIdNameResponseDTO.class,
//                        route.id,
//                        route.name
//                ))
//                .from(route)
//                .where(route.member.id.eq(memberId)
//                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(route.id.asc())
//                .fetch();

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

        long total = query
                .select(route.count())
                .from(route)
                .where(route.member.id.eq(memberId)
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                .fetchOne();

        return new PageImpl<>(routeDetails, pageable, total);
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

    public Map<Long, Integer> getIntegratedRouteLikeCounts(List<Long> integratedIds) {
        //특정 통합 경로의 좋아요 개수 구하기
        List<Tuple> results = query
                .select(integratedRoute.id, likePost.id.count().intValue())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .join(likePost).on(likePost.route.id.eq(route.id))
                .where(integratedRoute.id.in(integratedIds))
                .groupBy(integratedRoute.id)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(integratedRoute.id),
                        tuple -> tuple.get(likePost.id.count().intValue())
                ));
    }

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

    public Long findPostIdByIntegratedRouteId(Long integratedId) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId)
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)));

        return query
                .select(post.id)
                .from(post)
                .join(route).on(post.route.id.eq(route.id))
                .where(route.id.eq(minRouteId))
                .fetchOne();

    }

    public Map<Long, Long> findPostIdsByIntegratedRouteIds(List<Long> integratedIds) {
        List<Tuple> results = query
                .select(post.route.integratedRoute.id, post.id, likePost.id.count().as("likeCount"))
                .from(post)
                .join(likePost).on(likePost.post.id.eq(post.id))
                .where(post.route.integratedRoute.id.in(integratedIds)
                        .and(post.route.routeStatus.eq(RouteStatus.PUBLIC)))
                .groupBy(post.route.integratedRoute.id, post.id)
                .orderBy(post.route.integratedRoute.id.asc(), likePost.id.count().desc())
                .fetch();

        Map<Long, Long> resultMap = new LinkedHashMap<>(); // 순서가 유지되도록 LinkedHashMap 사용

        results.forEach(tuple -> resultMap.put(tuple.get(post.route.integratedRoute.id), tuple.get(post.id)));

        return resultMap;
    }

    public boolean findHashtagIdIdCity(Long hashtagId){

        return query
                .select(hashtag.isCity)
                .from(hashtag)
                .where(hashtag.id.eq(hashtagId))
                .fetchOne();
    }

}
