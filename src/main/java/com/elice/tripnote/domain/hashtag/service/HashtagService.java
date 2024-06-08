package com.elice.tripnote.domain.hashtag.service;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    //해시태그 저장
    @Transactional
    public HashtagResponseDTO saveHashtag(HashtagRequestDTO hashtagRequestDTO){

        log.info("hashtagRequestDTO.isCity = {}", hashtagRequestDTO.isCity());

        //해시태그명 중복 체크
        duplicateNameCheck(hashtagRequestDTO.getName());

        Hashtag hashtag = Hashtag.builder()
                .name(hashtagRequestDTO.getName())
                .isCity(hashtagRequestDTO.isCity())
                .build();

        hashtagRepository.save(hashtag);

        return hashtag.toResponseDTO();
    }

    //해시태그 수정
    //QueryDSL X ,  JPA(더티체킹) 사용  -> DB에 존재하는 모든 값들이 채워지지 않았을 경우 Null 값으로 치환되지 않음
    @Transactional
    public HashtagResponseDTO updateHashtag(Long id, HashtagRequestDTO hashtagRequestDTO){

        //해시태그 목록에서 선택하는 것이므로 null이 반환되지 않을 것으로 판단되어 get을 사용
        Hashtag hashtag = hashtagRepository.getById(id);

        //변경하려는 해시태그명 중복 체크
        duplicateNameCheck(hashtagRequestDTO.getName());

        hashtag.update(hashtagRequestDTO);

        return hashtag.toResponseDTO();
    }

    //해시태그 삭제 ( isDelete = true 로 변경 )
    @Transactional
    public boolean deleteHashtag(Long id){

        Hashtag hashtag = hashtagRepository.getById(id);

        hashtag.delete();

        Hashtag deleteHashtag = hashtagRepository.save(hashtag);

        return deleteHashtag.isDelete();

    }

    //해시태그명 중복 체크
    private void duplicateNameCheck(String name){

        boolean isDuplicate = hashtagRepository.existsByName(name);
        //해당 해시태그명이 이미 존재하는 경우 true 반환 -> 예외 처리
        if(!isDuplicate){
            return;
        }

        CustomException ex = new CustomException(ErrorCode.DUPLICATE_NAME);
        log.error("에러 발생: {}", ex.getMessage(), ex);
        throw ex;
    }
}
