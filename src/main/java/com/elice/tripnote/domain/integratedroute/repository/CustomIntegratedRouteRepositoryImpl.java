package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
import com.elice.tripnote.domain.route.entity.IntegratedRouteDTO;
import com.querydsl.core.types.Projections;
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

    public List<IntegratedRouteDTO> findTopIntegratedRoutesByRegionAndHashtags(IntegratedRouteStatus region, List<Long> hashtags) {
        JPQLQuery<LocalDateTime> maxStartAtSubquery = JPAExpressions.select(lbp.startAt.max())
                .from(lbp)
                .where(lbp.integratedRoute.id.eq(ir.id));

        return query
                .select(Projections.constructor(IntegratedRouteDTO.class,
                        ir.id,
                        lbp.likes.sum()
                ))
                .from(ir)
                .leftJoin(uh).on(ir.id.eq(uh.integratedRoute.id))
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(ir.region.eq(region)
                        .and(uh.hashtag.id.in(hashtags))
                        .and(lbp.startAt.eq(maxStartAtSubquery)) // 최대 startAt 값과 비교
                )
                .groupBy(ir.id)
                .having(uh.hashtag.id.countDistinct().eq((long) hashtags.size()))
                .orderBy(lbp.likes.sum().desc())
                .limit(5)
                .fetch();
    }

    public List<IntegratedRouteDTO> findIntegratedRouteFilterByHashtags(List<Long> integratedIds, List<Long> hashtags) {
        return query
                .select(Projections.constructor(IntegratedRouteDTO.class,
                        ir.id,
                        lbp.likes.sum()
                ))
                .from(ir)
                .leftJoin(uh).on(ir.id.eq(uh.integratedRoute.id))
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(
                        ir.id.in(integratedIds)
                                .and(uh.hashtag.id.in(hashtags))
                )
                .groupBy(ir.id)
                .having(
                        uh.hashtag.id.countDistinct().eq( (long)hashtags.size())
                                .and(ir.id.countDistinct().eq((long) integratedIds.size()))
                                .and(lbp.startAt.eq(lbp.startAt.max()))
                )
                .orderBy(lbp.likes.desc())
                .limit(5)
                .fetch();
    }
}
