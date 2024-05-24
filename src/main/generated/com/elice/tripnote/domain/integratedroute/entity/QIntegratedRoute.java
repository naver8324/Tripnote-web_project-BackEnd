package com.elice.tripnote.domain.integratedroute.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIntegratedRoute is a Querydsl query type for IntegratedRoute
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIntegratedRoute extends EntityPathBase<IntegratedRoute> {

    private static final long serialVersionUID = 132676333L;

    public static final QIntegratedRoute integratedRoute = new QIntegratedRoute("integratedRoute");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath integratedRoutes = createString("integratedRoutes");

    public final ListPath<com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod, com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod> likeBookmarkPeriods = this.<com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod, com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod>createList("likeBookmarkPeriods", com.elice.tripnote.domain.likebookmarkperiod.entity.LikeBookmarkPeriod.class, com.elice.tripnote.domain.likebookmarkperiod.entity.QLikeBookmarkPeriod.class, PathInits.DIRECT2);

    public final EnumPath<com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus> region = createEnum("region", com.elice.tripnote.domain.integratedroute.status.IntegratedRouteStatus.class);

    public final ListPath<com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag, com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag> uuidHashtags = this.<com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag, com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag>createList("uuidHashtags", com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag.class, com.elice.tripnote.domain.link.uuidhashtag.entity.QUUIDHashtag.class, PathInits.DIRECT2);

    public QIntegratedRoute(String variable) {
        super(IntegratedRoute.class, forVariable(variable));
    }

    public QIntegratedRoute(Path<? extends IntegratedRoute> path) {
        super(path.getType(), path.getMetadata());
    }

    public QIntegratedRoute(PathMetadata metadata) {
        super(IntegratedRoute.class, metadata);
    }

}

