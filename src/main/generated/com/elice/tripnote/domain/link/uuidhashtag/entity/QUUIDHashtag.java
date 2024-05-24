package com.elice.tripnote.domain.link.uuidhashtag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUUIDHashtag is a Querydsl query type for UUIDHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUUIDHashtag extends EntityPathBase<UUIDHashtag> {

    private static final long serialVersionUID = -1808053811L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUUIDHashtag uUIDHashtag = new QUUIDHashtag("uUIDHashtag");

    public final com.elice.tripnote.domain.hashtag.entity.QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute integratedRoute;

    public QUUIDHashtag(String variable) {
        this(UUIDHashtag.class, forVariable(variable), INITS);
    }

    public QUUIDHashtag(Path<? extends UUIDHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUUIDHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUUIDHashtag(PathMetadata metadata, PathInits inits) {
        this(UUIDHashtag.class, metadata, inits);
    }

    public QUUIDHashtag(Class<? extends UUIDHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hashtag = inits.isInitialized("hashtag") ? new com.elice.tripnote.domain.hashtag.entity.QHashtag(forProperty("hashtag")) : null;
        this.integratedRoute = inits.isInitialized("integratedRoute") ? new com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute(forProperty("integratedRoute")) : null;
    }

}

