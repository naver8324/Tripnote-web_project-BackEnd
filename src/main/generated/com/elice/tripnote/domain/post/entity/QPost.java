package com.elice.tripnote.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 2138024295L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final ListPath<com.elice.tripnote.domain.comment.entity.Comment, com.elice.tripnote.domain.comment.entity.QComment> Comments = this.<com.elice.tripnote.domain.comment.entity.Comment, com.elice.tripnote.domain.comment.entity.QComment>createList("Comments", com.elice.tripnote.domain.comment.entity.Comment.class, com.elice.tripnote.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.elice.tripnote.domain.link.likePost.entity.LikePost, com.elice.tripnote.domain.link.likePost.entity.QLikePost> likePosts = this.<com.elice.tripnote.domain.link.likePost.entity.LikePost, com.elice.tripnote.domain.link.likePost.entity.QLikePost>createList("likePosts", com.elice.tripnote.domain.link.likePost.entity.LikePost.class, com.elice.tripnote.domain.link.likePost.entity.QLikePost.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likes = createNumber("likes", Integer.class);

    public final com.elice.tripnote.domain.member.entity.QMember member;

    public final NumberPath<Integer> report = createNumber("report", Integer.class);

    public final com.elice.tripnote.domain.route.entity.QRoute route;

    public final StringPath title = createString("title");

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.elice.tripnote.domain.member.entity.QMember(forProperty("member")) : null;
        this.route = inits.isInitialized("route") ? new com.elice.tripnote.domain.route.entity.QRoute(forProperty("route"), inits.get("route")) : null;
    }

}

