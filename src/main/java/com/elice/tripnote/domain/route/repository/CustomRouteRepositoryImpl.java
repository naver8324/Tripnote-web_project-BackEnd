package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.post.entity.QPost;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteIdNameResponseDTO;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRouteRepositoryImpl implements CustomRouteRepository {
    private final JPAQueryFactory query;
    private final QRoute route = new QRoute("r");
    private final QRouteSpot routeSpot = new QRouteSpot("rs");
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

    public Page<RouteIdNameResponseDTO> findMarkedRoutesByMemberId(Long memberId, PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());


        QueryResults<RouteIdNameResponseDTO> queryResults = query
                .select(Projections.constructor(RouteIdNameResponseDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .join(bookmark).on(bookmark.route.id.eq(route.id))
                .where(bookmark.member.id.eq(memberId)
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(route.id.desc())
                .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageRequest, queryResults.getTotal());
    }


    public Page<RouteIdNameResponseDTO> findRoutesByMemberId(Long memberId, PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        //        return query
//                .select(Projections.constructor(RouteIdNameDTO.class,
//                        route.id,
//                        route.name
//                ))
//                .from(route)
//                .where(route.member.id.eq(memberId))
//                .fetch();

        QueryResults<RouteIdNameResponseDTO> results = query
                .select(Projections.constructor(RouteIdNameResponseDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .where(route.member.id.eq(memberId)
                        .and(route.routeStatus.eq(RouteStatus.PUBLIC)))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(route.id.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageRequest, results.getTotal());
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


    //이후 정렬 조건이 필요하다면 추가하세요!
    private OrderSpecifier<?> getOrderSpecifier(String order, boolean asc) {   //정렬 방식 정하기

        // 기본값 설정
        return asc ? route.id.asc() : route.id.desc();  //order에 값이 없을 경우 id를 기준으로 정렬
    }

    public boolean findHashtagIdIdCity(Long hashtagId){

        return query
                .select(hashtag.isCity)
                .from(hashtag)
                .where(hashtag.id.eq(hashtagId))
                .fetchOne();

    }

}
