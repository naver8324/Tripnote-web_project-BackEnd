package com.elice.tripnote.domain.comment.repository;


import com.elice.tripnote.domain.comment.entity.CommentResponseDTO;
import com.elice.tripnote.domain.comment.entity.QComment;
import com.elice.tripnote.domain.member.entity.QMember;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;


    private final QComment comment = QComment.comment;
    private final QMember member = QMember.member;







    public CommentResponseDTO customFindNotDeletedComment(Long commentId){



        CommentResponseDTO commentResponseDTO = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        member.nickname,
                        comment.content,
                        comment.createdAt,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.id.eq(commentId)
                        .and(comment.isDeleted.isFalse()))
                .fetchFirst();

        return commentResponseDTO;
    }



    public Page<CommentResponseDTO> customFindNotDeletedCommentsByPostId(Long postId, PageRequestDTO pageRequestDTO) {

        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());


        Long totalCount = query
                .select(comment.count())
                .from(comment)
                .where(comment.post.id.eq(postId)
                        .and(comment.isDeleted.isFalse()))
                .fetchFirst();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        member.nickname,
                        comment.content,
                        comment.createdAt,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .join(comment.member, member)
                .where(comment.post.id.eq(postId)
                        .and(comment.isDeleted.isFalse()))
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }


    public Page<CommentResponseDTO> customFindComments(Long commentId, PageRequestDTO pageRequestDTO){

        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());


        Long memberId = commentId != null? query.select(member.id).from(member).join(member.comments, comment).where(comment.id.eq(commentId)).fetchFirst() : null;

        Long totalCount =query
                .select(comment.count())
                .from(comment)
                .join(comment.member, member)
                .where(memberId != null ? member.id.eq(memberId) : null)
                .fetchFirst();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        member.nickname,
                        comment.content,
                        comment.createdAt,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .join(comment.member, member)
                .where(memberId != null ? member.id.eq(memberId) : null)
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }

    public Page<CommentResponseDTO> customFindComments(String nickname, PageRequestDTO pageRequestDTO){

        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());


        Long memberId = query.select(member.id).from(member).where(member.nickname.eq(nickname)).fetchFirst();

        if(memberId == null){
            memberId = -1L;
        }

        Long totalCount =query
                .select(comment.count())
                .from(comment)
                .join(comment.member, member)
                .where(member.id.eq(memberId))
                .fetchFirst();


        List<CommentResponseDTO> commentResponseDTOs = query
                .select(Projections.constructor(CommentResponseDTO.class,
                        comment.id,
                        member.nickname,
                        comment.content,
                        comment.createdAt,
                        comment.report,
                        comment.isDeleted
                ))
                .from(comment)
                .join(comment.member, member)
                .where(member.id.eq(memberId))
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(commentResponseDTOs, pageRequest, totalCount);
    }


    public void customDeleteCommentsByPostId(Long postId){


        Long count = query
                .update(comment)
                .set(comment.isDeleted, true)
                .where(comment.post.id.eq(postId))
                .execute();
        em.clear();
        em.flush();


    }

    private OrderSpecifier<?> getOrderSpecifier(String order, boolean asc) {   //정렬 방식 정하기

        // 기본값 설정
        return asc ? comment.id.asc() : comment.id.desc();  //order에 값이 없을 경우 id를 기준으로 정렬
    }


}
