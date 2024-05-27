package com.elice.tripnote.domain.hashtag.service;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
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

        Hashtag hashtag = Hashtag.builder()
                .name(hashtagRequestDTO.getName())
                .isCity(hashtagRequestDTO.isCity())
                .build();

        hashtagRepository.save(hashtag);

        return hashtag.toResponseDTO();
    }

    //해시태그 조회 - repository에서 dto로 값을 반환해 바로 controller단에서 조회
    //지역과 지역이 아닌 해시태그 분리해서 조회
    //지역 해시태그 조회 is_city = true ,  isDelete = false
//    public List<HashtagResponseDTO> getHashtagsByIsCityTrue() {
//
//        return hashtagRepository.findByIsCityAndDelete(true, false);
//    }

    //지역 아닌 해시태그 조회 is_city = false, isDelete = false
//    public List<HashtagResponseDTO> getHashtagsByIsCityFalse() {
//
//        return hashtagRepository.findByIsCityAndDelete(false, false);
//    }


    //해시태그 수정
    //QueryDSL X ,  JPA(더티체킹) 사용  -> DB에 존재하는 모든 값들이 채워지지 않았을 경우 Null 값으로 치환되지 않음
    @Transactional
    public HashtagResponseDTO updateHashtag(Long id, HashtagRequestDTO hashtagRequestDTO){

        //해시태그 목록에서 선택하는 것이므로 null이 반환되지 않을 것으로 판단되어 get을 사용
        Hashtag hashtag = hashtagRepository.getById(id);

        hashtag.update(hashtagRequestDTO);

        return hashtag.toResponseDTO();
    }

    //해시태그 삭제 ( isDelete = true 로 변경 )
    @Transactional
    public void deleteHashtag(Long id){

        Hashtag hashtag = hashtagRepository.getById(id);

        hashtag.delete();

    }
}
