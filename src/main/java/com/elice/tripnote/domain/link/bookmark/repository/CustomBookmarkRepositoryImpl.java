package com.elice.tripnote.domain.link.bookmark.repository;

import com.elice.tripnote.domain.link.bookmark.entity.QBookmark;
import com.elice.tripnote.domain.route.entity.QRoute;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomBookmarkRepositoryImpl implements CustomBookmarkRepository{
    private final JPAQueryFactory query;
    private final QBookmark bookmark = new QBookmark ("b");
    private final QRoute route = new QRoute("r");

    public int getBookmarkCount(Long integratedRouteId){
        Long count = query
                .select(bookmark.count())
                .from(bookmark)
                .join(route).on(route.id.eq(bookmark.route.id))
                .where(route.id.eq(integratedRouteId))
                .fetchOne();
        return count != null ? count.intValue() : 0;
    }

}
