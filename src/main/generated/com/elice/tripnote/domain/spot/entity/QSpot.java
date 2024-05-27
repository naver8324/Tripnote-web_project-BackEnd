package com.elice.tripnote.domain.spot.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpot is a Querydsl query type for Spot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpot extends EntityPathBase<Spot> {

    private static final long serialVersionUID = -158669013L;

    public static final QSpot spot = new QSpot("spot");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final StringPath location = createString("location");

    public final StringPath region = createString("region");

    public QSpot(String variable) {
        super(Spot.class, forVariable(variable));
    }

    public QSpot(Path<? extends Spot> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpot(PathMetadata metadata) {
        super(Spot.class, metadata);
    }

}

