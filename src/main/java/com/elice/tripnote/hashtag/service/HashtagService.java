package com.elice.tripnote.hashtag.service;

import com.elice.tripnote.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.hashtag.entity.Hashtag;
import com.elice.tripnote.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    //해시태그 조회
    //지역과 지역이 아닌 해시태그 분리해서 조회
    //지역 해시태그 조회 is_city = "Y"
    @Transactional(readOnly = true)
    public List<HashtagResponseDTO> getHashtagsByIsCity() {

        return hashtagRepository.findHashtagsByIsCity()
                .stream()
                .map(Hashtag::toResponseDTO)
                .collect(Collectors.toList());
    }

    //지역 아닌 해시태그 조회 is_city = "N"
    @Transactional(readOnly = true)
    public List<HashtagResponseDTO> getHashtagsByIsNotCity() {

        return hashtagRepository.findHashtagsByIsNotCity()
                .stream()
                .map(Hashtag::toResponseDTO)
                .collect(Collectors.toList());
    }

    //해시태그 저장
    public HashtagResponseDTO saveHashtag(HashtagRequestDTO hashtagRequestDTO){

        Hashtag hashtag = Hashtag.builder()
                .name(hashtagRequestDTO.getName())
                .isCity(hashtagRequestDTO.getIsCity())
                .build();

        hashtagRepository.save(hashtag);

        return hashtag.toResponseDTO();
    }

    //해시태그 수정
    public HashtagResponseDTO updateHashtag(Long id, HashtagRequestDTO hashtagRequestDTO){

        //해시태그 목록에서 선택하는 것이므로 null이 반환되지 않을 것으로 판단되어 get을 사용
        Hashtag hashtag = hashtagRepository.getById(id);

        hashtag.update(hashtagRequestDTO);

        return hashtag.toResponseDTO();
    }

    //해시태그 삭제
    public void deleteHashtag(Long id){

        hashtagRepository.deleteById(id);
    }
}
