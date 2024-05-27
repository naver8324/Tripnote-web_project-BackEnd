package com.elice.tripnote.domain.hashtag.controller;

import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.domain.hashtag.service.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/hashtags")
public class HashtagController implements SwaggerHashtagController{

    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;

    //지역 해시태그 조회 is_city = true ,  isDelete = false
    @Override
    @GetMapping("/isCity")
    public ResponseEntity<List<HashtagResponseDTO>> getHashtagsByIsCityTrue(){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagRepository.findByIsCityAndIsDelete(true, false));
    }

    //지역 아닌 해시태그 조회 is_city = false, isDelete = false
    @Override
    @GetMapping("/isNotCity")
    public ResponseEntity<List<HashtagResponseDTO>> getHashtagsByIsCityFalse(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagRepository.findByIsCityAndIsDelete(false, false));
    }

    //해시태그 생성
    @Override
    @PostMapping("/create")
    public ResponseEntity<HashtagResponseDTO> createHashtag(@RequestBody HashtagRequestDTO hashtagRequestDTO){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagService.saveHashtag(hashtagRequestDTO));
    }

    //해시태그 수정
    @Override
    @PatchMapping("/update/{id}")
    public ResponseEntity<HashtagResponseDTO> updateHashtag(@PathVariable Long id, @RequestBody HashtagRequestDTO hashtagRequestDTO){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagService.updateHashtag(id, hashtagRequestDTO));
    }

    //해시태그 삭제
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable Long id){

        hashtagService.deleteHashtag(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
