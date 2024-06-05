package com.elice.tripnote.domain.hashtag.repository;

import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.entity.QHashtag;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
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

    public List<HashtagDTO> customFindAll(){

        return query
                .select(Projections.constructor(HashtagDTO.class,
                        hashtag.id,
                        hashtag.name,
                        hashtag.isCity,
                        hashtag.isDelete
                ))
                .from(hashtag)
                .fetch();
    }

}