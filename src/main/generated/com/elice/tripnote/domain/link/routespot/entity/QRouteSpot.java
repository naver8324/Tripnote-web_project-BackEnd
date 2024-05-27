package com.elice.tripnote.domain.link.routespot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRouteSpot is a Querydsl query type for RouteSpot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRouteSpot extends EntityPathBase<RouteSpot> {

    private static final long serialVersionUID = 1808836269L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRouteSpot routeSpot = new QRouteSpot("routeSpot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> nextSpotId = createNumber("nextSpotId", Long.class);

    public final com.elice.tripnote.domain.route.entity.QRoute route;

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public final com.elice.tripnote.domain.spot.entity.QSpot spot;

    public QRouteSpot(String variable) {
        this(RouteSpot.class, forVariable(variable), INITS);
    }

    public QRouteSpot(Path<? extends RouteSpot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRouteSpot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRouteSpot(PathMetadata metadata, PathInits inits) {
        this(RouteSpot.class, metadata, inits);
    }

    public QRouteSpot(Class<? extends RouteSpot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.route = inits.isInitialized("route") ? new com.elice.tripnote.domain.route.entity.QRoute(forProperty("route"), inits.get("route")) : null;
        this.spot = inits.isInitialized("spot") ? new com.elice.tripnote.domain.spot.entity.QSpot(forProperty("spot")) : null;
    }

}

