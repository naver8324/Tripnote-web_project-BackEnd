package com.elice.tripnote.domain.integratedroute.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
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

    public List<Long> findTopIntegratedRoutesByRegionAndHashtags(Region region/*, List<Long> hashtags*/) {
        JPQLQuery<LocalDateTime> maxStartAtSubquery = JPAExpressions.select(lbp.startAt.max())
                .from(lbp)
                .where(lbp.integratedRoute.id.eq(ir.id));

        return query
                .select(/*Projections.constructor(IntegratedRouteDTO.class,*/
                        ir.id/*,
                        lbp.likes.sum()
                )*/)
                .from(ir)
                //.leftJoin(uh).on(ir.id.eq(uh.integratedRoute.id))
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(ir.region.eq(region)
                        //        .and(uh.hashtag.id.in(hashtags))
                        .and(lbp.startAt.eq(maxStartAtSubquery)) // 최대 startAt 값과 비교
                )
                .groupBy(ir.id)
                //.having(uh.hashtag.id.countDistinct().eq((long) hashtags.size()))
                .orderBy(lbp.likes.desc())
                .limit(5)
                .fetch();
        /*
        처음 만들었던 거
        SELECT ir.id AS integrated_route_id, SUM(plb.likes) AS total_likes
        FROM integrated_route ir
        left JOIN uuid_hashtag uh ON ir.id = uh.integrated_route_id
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        WHERE ir.region = :region
          AND uh.hashtag_id IN :hashtags  -- 제시된 해시태그 id 안에 속하는 row만 남김
        GROUP BY ir.id
        HAVING COUNT(DISTINCT uh.hashtag_id) = :hashtags_size  -- 그룹별로 묶었을 때, 해당 그룹의 해시태그 id 개수가 hashtags의 개수와 같으면 모든 hashtag가 포함된거?
            and lbp.started_at = max(lbp.started_at) -- 그룹별로 묶었을 때 started_at 값이 가장 큰 row만 남게
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */

        /*
        새로 만든 쿼리
        SELECT ir.id, SUM(plb.likes), count(lp.id)
        FROM integrated_route ir
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        join route r on r.integrated_id=ir.id
        join like_post lp on r.id=lp.route_id
        WHERE ir.region = :region
        GROUP BY ir.id
            and lbp.started_at = max(lbp.started_at) -- 그룹별로 묶었을 때 started_at 값이 가장 큰 row만 남게
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */
    }



    public List<Long> findIntegratedRoute(List<Long> integratedIds/*, List<Long> hashtags*/) {
        /*

        SELECT ir.id AS integrated_route_id, SUM(plb.likes) AS total_likes
        FROM integrated_route ir
        left JOIN uuid_hashtag uh ON ir.id = uh.integrated_route_id
        JOIN like_bookmark_period lbp ON ir.id = lbp.integrated_route_id
        WHERE ir.id in :ids
          AND uh.hashtag_id IN :hashtags  -- 제시된 해시태그 id 안에 속하는 row만 남김
        GROUP BY ir.id
        HAVING COUNT(DISTINCT uh.hashtag_id) = :hashtags.size  -- 그룹별로 묶었을 때, 해당 그룹의 해시태그 id 개수가 hashtags의 개수와 같으면 모든 hashtag가 포함된거?
            and count(distinct ir.id) = :ids.size
            and lbp.started_at = max(lbp.started_at) -- 그룹별로 묶었을 때 started_at 값이 가장 큰 row만 남게
        ORDER BY lbp.likes DESC
        LIMIT 5;
         */
        JPQLQuery<LocalDateTime> maxStartAtSubquery = JPAExpressions.select(lbp.startAt.max())
                .from(lbp)
                .where(lbp.integratedRoute.id.eq(ir.id))
                .groupBy(lbp.integratedRoute.id);
/*
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
                                .and(lbp.startAt.eq(maxStartAtSubquery))
                )
                .groupBy(ir.id)
                .having(
                        uh.hashtag.id.countDistinct().eq((long) hashtags.size())
                )
                .orderBy(lbp.likes.sum().desc())
                .limit(5)
                .fetch();*/
        return query
                .select(ir.id)
                .from(ir)
                .join(lbp).on(ir.id.eq(lbp.integratedRoute.id))
                .where(
                        ir.id.in(integratedIds)
                                .and(lbp.startAt.eq(maxStartAtSubquery))
                )
                .groupBy(ir.id)
                .orderBy(lbp.likes.desc())
                .limit(5)
                .fetch();


    }
}
