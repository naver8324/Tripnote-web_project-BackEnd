package com.elice.tripnote.domain.member.repository;


import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.domain.member.entity.QMember;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory query;

    private final QMember member = new QMember("member");

    public Page<MemberResponseDTO> customFindAll(PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        List<MemberResponseDTO> memberResponseDTOS = query
                .select(Projections.constructor(MemberResponseDTO.class,
                        member.id,
                        member.email,
                        member.password,
                        member.oauthId,
                        member.oauthType,
                        member.nickname,
                        member.deletedAt,
                        member.status
                ))
                .from(member)
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = query
                .select(member.count())
                .from(member)
                .fetchFirst();


        return new PageImpl<>(memberResponseDTOS, pageRequest, totalCount);
    }

    private OrderSpecifier<?> getOrderSpecifier(String order, boolean asc) {   //정렬 방식 정하기
        //asc 가 true, false인지 를 구분하여 정렬 함

        // 기본값 설정
        return asc ? member.id.asc() : member.id.desc();  //order에 값이 없을 경우 id를 기준으로 정렬
    }


}
