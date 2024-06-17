package com.elice.tripnote.domain.hashtag.repository;

import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.elice.tripnote.global.entity.PageRequestDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class HashtagCustomRepositoryImpl implements HashtagCustomRepository {

    private final JPAQueryFactory query;

    private final QHashtag hashtag = new QHashtag("hashtag");

    public List<HashtagResponseDTO> findByIsCityAndIsDelete(boolean isCity, boolean isDelete){

        return query
                .select(Projections.constructor(HashtagResponseDTO.class,
                        hashtag.id,
                        hashtag.name,
                        hashtag.isCity
                ))
                .from(hashtag)
                .where(hashtag.isCity.eq(isCity)
                        .and(hashtag.isDelete.eq(isDelete)))
                .fetch();
    }

    public Page<HashtagDTO> customFindAll(PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage()-1, pageRequestDTO.getSize());
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(pageRequestDTO.getOrder(), pageRequestDTO.isAsc());

        log.info("pageRequestDTO ={}", pageRequestDTO.getPage());
        List<HashtagDTO> hashtagDTOS = query
                .select(Projections.constructor(HashtagDTO.class,
                        hashtag.id,
                        hashtag.name,
                        hashtag.isCity,
                        hashtag.isDelete
                ))
                .from(hashtag)
                .orderBy(orderSpecifier)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        long totalCount = query
                .select(hashtag.count())
                .from(hashtag)
                .fetchFirst();


        return new PageImpl<>(hashtagDTOS, pageRequest, totalCount);
    }

    private OrderSpecifier<?> getOrderSpecifier(String order, boolean asc) {   //정렬 방식 정하기
        //desc 가 true, false인지 를 구분하여 정렬 함

        if ("id".equals(order)) {   //만약 sort가 "id"인 경우
            return asc ? hashtag.id.asc() : hashtag.id.desc();   //desc가 true일 때 : hashtag.id.desc() 를 orderSpecifier<?>에 저장
        } else if ("name".equals(order)) {
            return asc ? hashtag.name.asc() : hashtag.name.desc();
        }
        // 기본값 설정
        return asc ? hashtag.id.asc() : hashtag.id.desc();  //order에 값이 없을 경우 id를 기준으로 정렬
    }

}