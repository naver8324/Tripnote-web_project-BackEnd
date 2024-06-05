package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.entity.Route;
import com.elice.tripnote.domain.route.entity.RouteIdNameDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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

    public List<RouteIdNameDTO> findLikedRoutesByMemberId(Long memberId) {
        return query
                .select(Projections.constructor(RouteIdNameDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .join(likePost).on(likePost.route.id.eq(route.id))
                .where(likePost.member.id.eq(memberId)
                        .and(route.id.eq(memberId)))
                .fetch();
    }

    public List<RouteIdNameDTO> findMarkedRoutesByMemberId(Long memberId) {
        return query
                .select(Projections.constructor(RouteIdNameDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .join(bookmark).on(bookmark.route.id.eq(route.id))
                .where(bookmark.member.id.eq(memberId)
                        .and(route.id.eq(memberId)))
                .fetch();
    }

    public List<RouteIdNameDTO> findRoutesByMemberId(Long memberId) {
        return query
                .select(Projections.constructor(RouteIdNameDTO.class,
                        route.id,
                        route.name
                ))
                .from(route)
                .where(route.id.eq(memberId))
                .fetch();
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
        return query
                .select(likePost.id.count().intValue())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .join(likePost).on(likePost.route.id.eq(route.id))
                .where(integratedRoute.id.eq(integratedRouteId))
                .groupBy(integratedRoute.id)
                .fetchOne();
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

}
