package com.elice.tripnote.domain.route.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoute is a Querydsl query type for Route
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoute extends EntityPathBase<Route> {

    private static final long serialVersionUID = -1862485203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoute route = new QRoute("route");

    public final NumberPath<Integer> expense = createNumber("expense", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute integratedRoute;

    public final com.elice.tripnote.domain.member.entity.QMember member;

    public final EnumPath<com.elice.tripnote.domain.route.status.RouteStatus> routeStatus = createEnum("routeStatus", com.elice.tripnote.domain.route.status.RouteStatus.class);

    public QRoute(String variable) {
        this(Route.class, forVariable(variable), INITS);
    }

    public QRoute(Path<? extends Route> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoute(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoute(PathMetadata metadata, PathInits inits) {
        this(Route.class, metadata, inits);
    }

    public QRoute(Class<? extends Route> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.integratedRoute = inits.isInitialized("integratedRoute") ? new com.elice.tripnote.domain.integratedroute.entity.QIntegratedRoute(forProperty("integratedRoute")) : null;
        this.member = inits.isInitialized("member") ? new com.elice.tripnote.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

