package com.elice.tripnote.domain.link.likePost.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod;
import com.elice.tripnote.domain.link.likePost.entity.QLikePost;
import com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag;
import com.elice.tripnote.domain.route.entity.QRoute;
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
public class CustomLikePostRepositoryImpl implements CustomLikePostRepository {
    private final JPAQueryFactory query;
    private final QIntegratedRoute integratedRoute = new QIntegratedRoute("ir");
    private final QRoute route = new QRoute("r");
    private final QLikePost likePost = new QLikePost("lp");

    @Override
    public boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId));

        return query
                .select(likePost.id)
                .from(likePost)
                .join(route).on(likePost.route.id.eq(route.id))
                .where(route.id.eq(minRouteId).and(route.member.id.eq(memberId)))
                .fetchCount() > 0;

        /*
        select count(lp.id) > 0
        from like_post lp
        join route r on r.id=lp.route_id
        join integrated_route ir on ir.id=r.integrated_route_id
        where r.member_id=:memberId and r.id=(
            select min(r.id)
            from route r
            join integrated_route ir on ir.id=r.integrated_id
        )
         */

    }

    @Override
    public void deleteByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId));

        query.delete(likePost)
                .where(likePost.route.id.eq(minRouteId)
                        .and(likePost.route.member.id.eq(memberId)))
                .execute();
        /*
        delete from like_post lp
        join route r on r.id=lp.route_id
        where route.member_id=:memberId and route.id=:minRouteId
         */
    }


}
