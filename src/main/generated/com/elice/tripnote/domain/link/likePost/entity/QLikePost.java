package com.elice.tripnote.domain.link.likePost.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikePost is a Querydsl query type for LikePost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikePost extends EntityPathBase<LikePost> {

    private static final long serialVersionUID = -236610187L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikePost likePost = new QLikePost("likePost");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> likedAt = createDateTime("likedAt", java.time.LocalDateTime.class);

    public final com.elice.tripnote.domain.member.entity.QMember member;

    public final com.elice.tripnote.domain.post.entity.QPost post;

    public QLikePost(String variable) {
        this(LikePost.class, forVariable(variable), INITS);
    }

    public QLikePost(Path<? extends LikePost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikePost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikePost(PathMetadata metadata, PathInits inits) {
        this(LikePost.class, metadata, inits);
    }

    public QLikePost(Class<? extends LikePost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.elice.tripnote.domain.member.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new com.elice.tripnote.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

