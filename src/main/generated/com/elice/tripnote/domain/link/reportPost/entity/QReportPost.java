package com.elice.tripnote.domain.link.reportPost.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportPost is a Querydsl query type for ReportPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportPost extends EntityPathBase<ReportPost> {

    private static final long serialVersionUID = -1529271185L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportPost reportPost = new QReportPost("reportPost");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.elice.tripnote.domain.member.entity.QMember member;

    public final com.elice.tripnote.domain.post.entity.QPost post;

    public final DateTimePath<java.time.LocalDateTime> reportedAt = createDateTime("reportedAt", java.time.LocalDateTime.class);

    public QReportPost(String variable) {
        this(ReportPost.class, forVariable(variable), INITS);
    }

    public QReportPost(Path<? extends ReportPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportPost(PathMetadata metadata, PathInits inits) {
        this(ReportPost.class, metadata, inits);
    }

    public QReportPost(Class<? extends ReportPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.elice.tripnote.domain.member.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new com.elice.tripnote.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

