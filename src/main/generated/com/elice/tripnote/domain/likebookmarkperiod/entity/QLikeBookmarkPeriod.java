package com.elice.tripnote.domain.likebookmarkperiod.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeBookmarkPeriod is a Querydsl query type for LikeBookmarkPeriod
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeBookmarkPeriod extends EntityPathBase<LikeBookmarkPeriod> {

    private static final long serialVersionUID = -1142739773L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeBookmarkPeriod likeBookmarkPeriod = new QLikeBookmarkPeriod("likeBookmarkPeriod");

    public final NumberPath<Integer> bookmark = createNumber("bookmark", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endAt = createDateTime("endAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute integratedRoute;

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startAt = createDateTime("startAt", java.time.LocalDateTime.class);

    public QLikeBookmarkPeriod(String variable) {
        this(LikeBookmarkPeriod.class, forVariable(variable), INITS);
    }

    public QLikeBookmarkPeriod(Path<? extends LikeBookmarkPeriod> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeBookmarkPeriod(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeBookmarkPeriod(PathMetadata metadata, PathInits inits) {
        this(LikeBookmarkPeriod.class, metadata, inits);
    }

    public QLikeBookmarkPeriod(Class<? extends LikeBookmarkPeriod> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.integratedRoute = inits.isInitialized("integratedRoute") ? new com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute(forProperty("integratedRoute")) : null;
    }

}

