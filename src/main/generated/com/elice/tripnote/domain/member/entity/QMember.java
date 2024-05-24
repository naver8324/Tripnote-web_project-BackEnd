package com.elice.tripnote.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1900662683L;

    public static final QMember member = new QMember("member1");

    public final ListPath<com.elice.tripnote.domain.link.bookmark.entity.Bookmark, com.elice.tripnote.domain.link.bookmark.entity.QBookmark> bookmarks = this.<com.elice.tripnote.domain.link.bookmark.entity.Bookmark, com.elice.tripnote.domain.link.bookmark.entity.QBookmark>createList("bookmarks", com.elice.tripnote.domain.link.bookmark.entity.Bookmark.class, com.elice.tripnote.domain.link.bookmark.entity.QBookmark.class, PathInits.DIRECT2);

    public final ListPath<com.elice.tripnote.domain.comment.entity.Comment, com.elice.tripnote.domain.comment.entity.QComment> comments = this.<com.elice.tripnote.domain.comment.entity.Comment, com.elice.tripnote.domain.comment.entity.QComment>createList("comments", com.elice.tripnote.domain.comment.entity.Comment.class, com.elice.tripnote.domain.comment.entity.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.elice.tripnote.domain.link.likePost.entity.LikePost, com.elice.tripnote.domain.link.likePost.entity.QLikePost> likePosts = this.<com.elice.tripnote.domain.link.likePost.entity.LikePost, com.elice.tripnote.domain.link.likePost.entity.QLikePost>createList("likePosts", com.elice.tripnote.domain.link.likePost.entity.LikePost.class, com.elice.tripnote.domain.link.likePost.entity.QLikePost.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Long> oauthId = createNumber("oauthId", Long.class);

    public final StringPath password = createString("password");

    public final ListPath<com.elice.tripnote.domain.post.entity.Post, com.elice.tripnote.domain.post.entity.QPost> posts = this.<com.elice.tripnote.domain.post.entity.Post, com.elice.tripnote.domain.post.entity.QPost>createList("posts", com.elice.tripnote.domain.post.entity.Post.class, com.elice.tripnote.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath state = createString("state");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

