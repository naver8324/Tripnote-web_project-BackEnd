package com.elice.tripnote.domain.route.repository;

import com.elice.tripnote.domain.link.routespot.entity.QRouteSpot;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRouteRepositoryImpl implements CustomRouteRepository {
    private final JPAQueryFactory query;
    private final QRoute route = new QRoute ("r");
    private final QRouteSpot routeSpot = new QRouteSpot ("rs");

    public List<Long> findIntegratedRouteIdsBySpots(List<Long> spots){
        return query
                .selectDistinct(route.integratedRoute.id)
                .from(route)
                .join(routeSpot).on(routeSpot.route.id.eq(route.id))
                .where(routeSpot.spot.id.in(spots))
                .groupBy(route.id, route.integratedRoute.id)
                .having(routeSpot.spot.id.countDistinct().eq((long) spots.size()))
                .fetch();
    }

}
