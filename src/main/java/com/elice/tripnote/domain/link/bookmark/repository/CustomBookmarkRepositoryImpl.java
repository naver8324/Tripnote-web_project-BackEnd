package com.elice.tripnote.domain.link.bookmark.repository;

import com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute;
import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBookmarkRepositoryImpl implements CustomBookmarkRepository{
    private final JPAQueryFactory query;
    private final QBookmark bookmark = new QBookmark ("b");
    private final QRoute route = new QRoute("r");
    private final QIntegratedRoute integratedRoute = new QIntegratedRoute("ir");

    public int getBookmarkCount(Long integratedRouteId){
        Long count = query
                .select(bookmark.count())
                .from(bookmark)
                .join(route).on(route.id.eq(bookmark.route.id))
                .where(route.id.eq(integratedRouteId))
                .fetchOne();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public boolean existsByMemberIdAndIntegratedRouteId(Long memberId, Long integratedId) {
        JPQLQuery<Long> minRouteId = JPAExpressions
                .select(route.id.min())
                .from(route)
                .join(integratedRoute).on(integratedRoute.id.eq(route.integratedRoute.id))
                .where(integratedRoute.id.eq(integratedId));

        return query
                .select(bookmark.id)
                .from(bookmark)
                .join(route).on(bookmark.route.id.eq(route.id))
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

        query.delete(bookmark)
                .where(bookmark.route.id.eq(minRouteId)
                        .and(bookmark.route.member.id.eq(memberId)))
                .execute();
        /*
        delete from like_post lp
        join route r on r.id=lp.route_id
        where route.member_id=:memberId and route.id=:minRouteId
         */
    }

}
