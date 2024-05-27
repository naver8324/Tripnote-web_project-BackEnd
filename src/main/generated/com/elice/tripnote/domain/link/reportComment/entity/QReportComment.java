package com.elice.tripnote.domain.link.reportComment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportComment is a Querydsl query type for ReportComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportComment extends EntityPathBase<ReportComment> {

    private static final long serialVersionUID = -1572629363L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportComment reportComment = new QReportComment("reportComment");

    public final com.elice.tripnote.domain.comment.entity.QComment comment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.elice.tripnote.domain.member.entity.QMember member;

    public final DateTimePath<java.time.LocalDateTime> reportedAt = createDateTime("reportedAt", java.time.LocalDateTime.class);

    public QReportComment(String variable) {
        this(ReportComment.class, forVariable(variable), INITS);
    }

    public QReportComment(Path<? extends ReportComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportComment(PathMetadata metadata, PathInits inits) {
        this(ReportComment.class, metadata, inits);
    }

    public QReportComment(Class<? extends ReportComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new com.elice.tripnote.domain.comment.entity.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new com.elice.tripnote.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

