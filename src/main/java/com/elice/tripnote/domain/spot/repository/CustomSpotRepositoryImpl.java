package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import com.elice.tripnote.domain.spot.entity.QSpot;
import com.elice.tripnote.domain.spot.entity.Spot;
import com.elice.tripnote.domain.spot.entity.SpotRegionDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomSpotRepositoryImpl implements CustomSpotRepository{
    private final JPAQueryFactory query;

    private final QSpot spot = new QSpot("s");
    private final QRouteSpot routeSpot = new QRouteSpot("rs");
    private final QRoute route = new QRoute("r");
    private final QIntegratedRoute integratedRoute =new QIntegratedRoute("ir");

    public List<SpotResponseDTO> findByRouteIds(Long routeId){
         /*
        select s.location, s.경로, s.위도
        from spot s
        join route_spot rs on rs.spot_id=s.id
        where rs.route_id=:routeId
        order by rs.sequence asc
         */

        return query
                .select(Projections.constructor(SpotResponseDTO.class,
                        spot.location,
                        spot.lat,
                        spot.lng
                ))
                .from(spot)
                .join(routeSpot).on(routeSpot.spot.id.eq(spot.id))
                .where(routeSpot.route.id.eq(routeId))
                .orderBy(routeSpot.sequence.asc())
                .fetch();


    }

    public SpotRegionDTO getRegionByspotId(Long spotId){
        return query
                .select(Projections.constructor(SpotRegionDTO.class,
                        spot.region
                ))
                .from(spot)
                .where(spot.id.eq(spotId))
                .fetchOne();

    }

//    public List<Spot> findSpotsByRouteIdInOrder(Long routeId){
//        return query
//                .select(spot)
//                .from(spot)
//                .join(routeSpot).on(routeSpot.spot.id.eq(spot.id))
//                .where(routeSpot.route.id.eq(routeId))
//                .orderBy(routeSpot.sequence.asc())
//                .fetch();
//    }

    public List<Spot> findSpotsByIntegratedRouteIdInOrder(Long integratedRouteId){
        // integratedRouteId와 같은 통합 경로 id를 가지는 아무 routeId 구하기
        JPQLQuery<Long> subquery = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(route.integratedRoute.id.eq(integratedRoute.id))
                .where(route.integratedRoute.id.eq(integratedRouteId));

        return query
                .select(spot)
                .from(spot)
                .join(routeSpot).on(routeSpot.spot.id.eq(spot.id))
                .join(route).on(routeSpot.route.id.eq(route.id))
                .where(routeSpot.route.id.eq(subquery))
                .orderBy(routeSpot.sequence.asc())
                .fetch();
    }

    public Map<Long, List<Spot>> findSpotsByIntegratedRouteIds(List<Long> integratedIds) {
        // 각 integratedRouteId에 해당하는 최소 routeId 찾기
        JPQLQuery<Long> minRouteIdsSubquery = JPAExpressions
                .select(route.id.min())
                .from(route)
//                .join(routeSpot).on(routeSpot.route.id.eq(route.id))
                .join(integratedRoute).on(route.integratedRoute.id.eq(integratedRoute.id))
                .where(integratedRoute.id.in(integratedIds))
                .groupBy(integratedRoute.id);

        List<Tuple> results = query
                .select(route.integratedRoute.id, routeSpot.spot)
                .from(routeSpot)
                .join(route).on(routeSpot.route.id.eq(route.id))
//                .join(integratedRoute).on(route.integratedRoute.id.eq(integratedRoute.id))
                .where(route.id.in(minRouteIdsSubquery))
                .orderBy(routeSpot.sequence.asc())
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(integratedRoute.id),
                        Collectors.mapping(tuple -> tuple.get(routeSpot.spot), Collectors.toList())
                ));

    }



}
