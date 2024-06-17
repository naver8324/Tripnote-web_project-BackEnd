package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.elice.tripnote.domain.route.status.RouteStatus;
import com.elice.tripnote.domain.spot.constant.Region;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomIntegratedRouteRepositoryImpl implements CustomIntegratedRouteRepository {
    private final JPAQueryFactory query;
    private final QIntegratedRoute ir = new QIntegratedRoute("ir");
    private final QUUIDHashtag uh = new QUUIDHashtag("uh");
    private final QLikeBookmarkPeriod lbp = new QLikeBookmarkPeriod("lbp");
    private final QRoute r = new QRoute("r");

    public List<Long> findTopIntegratedRoutesByRegionAndHashtags(Region region) {
        //할거면 해당 integrated route id의 가장 최근 statAt 값을 구해야함. 리스트로
//        JPQLQuery<LocalDateTime> maxStartAtSubquery = JPAExpressions.select(lbp.startAt.max())
//                .from(lbp)
//                .where(lbp.integratedRoute.id.eq(ir.id));

        return query
                .select(ir.id)
                .from(ir)
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(ir.region.eq(region)
//                        .and(lbp.startAt.eq(maxStartAtSubquery)) // 최대 startAt 값과 비교
                        .and(ir.routeStatus.eq(RouteStatus.PUBLIC))
                )
                .groupBy(ir.id)
                .orderBy(lbp.likes.sum().desc())
                .limit(5)
                .fetch();
    }

    public void deleteIntegratedRoute(Long integratedRouteId){
        //route 삭제 처리되고, 해당 route의 integrated route에 가서
        // 연관된 public route가 1개 이상인지 확인
        // 만약 0개라면 delete 처리하기

        long count = query
                .select(ir.id)
                .from(ir)
                .join(r).on(r.integratedRoute.id.eq(ir.id))
                .where(ir.id.eq(integratedRouteId)
                        .and(r.routeStatus.eq(RouteStatus.PUBLIC)))
                .fetchCount();

        if(count < 1){
            query.update(ir)
                    .set(ir.routeStatus, RouteStatus.DELETE)
                    .where(ir.id.eq(integratedRouteId))
                    .execute();
        }

    }
}
