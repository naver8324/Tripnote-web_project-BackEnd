package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
import com.elice.tripnote.domain.route.entity.IntegratedRouteRegionDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomIntegratedRouteRepositoryImpl implements CustomIntegratedRouteRepository{
    private final JPAQueryFactory query;
    private final QIntegratedRoute ir = new QIntegratedRoute ("ir");
    private final QUUIDHashtag uh = new QUUIDHashtag("uh");
    private final QLikeBookmarkPeriod lbp = new QLikeBookmarkPeriod("lbp");

    public List<IntegratedRouteRegionDTO> findTopIntegratedRoutesByRegionAndHashtags(IntegratedRouteStatus region, List<Long> hashtags){
        return query
                .select(Projections.constructor(IntegratedRouteRegionDTO.class,
                        ir.id,
                        lbp.likes.sum()
                ))
                .from(ir)
                .leftJoin(uh).on(ir.id.eq(uh.integratedRoute.id))
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(ir.region.eq(region)
                        .and(uh.hashtag.id.in(hashtags))
                )
                .groupBy(ir.id)
                .having(uh.hashtag.id.countDistinct().eq((long) hashtags.size())
                        .and(lbp.startAt.eq(lbp.startAt.max()))
                )
                .orderBy(lbp.likes.desc())
                .limit(5)
                .fetch();
    }
}
