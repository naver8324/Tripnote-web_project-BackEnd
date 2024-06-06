package com.elice.tripnote.domain.hashtag.controller;

import com.elice.tripnote.domain.hashtag.entity.Hashtag;
import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.domain.hashtag.repository.HashtagRepository;
import com.elice.tripnote.domain.hashtag.service.HashtagService;
import com.elice.tripnote.global.annotation.AdminRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HashtagController implements SwaggerHashtagController{

    private final HashtagService hashtagService;
    private final HashtagRepository hashtagRepository;

    //전체 해시태그 조회
    @Override
    @GetMapping("/admin/hashtags")
    public ResponseEntity<Page<HashtagDTO>> getHashtags(@RequestParam(name = "page", defaultValue = "0") int page,
                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                        @RequestParam(name = "sort", defaultValue = "id") String sort){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagRepository.customFindAll(page, size, sort));
    }


    //지역 해시태그 조회 isCity의 = true ,  isDelete = false
    //지역 아닌 해시태그 조회 isCity의 = false, isDelete = false
    //프론트에서 isCity의 값을 받아온다
    @Override
    @GetMapping("/hashtags/isCity")
    public ResponseEntity<List<HashtagResponseDTO>> getHashtagsByIsCityTrue(@RequestParam(name = "isCity") boolean isCity){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagRepository.findByIsCityAndIsDelete(isCity, false));
    }

    //해시태그 생성
    @Override
    @AdminRole
    @PostMapping("/admin/hashtags/create")
    public ResponseEntity<HashtagResponseDTO> createHashtag(@RequestBody HashtagRequestDTO hashtagRequestDTO){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagService.saveHashtag(hashtagRequestDTO));
    }

    //해시태그 수정
    @Override
    @AdminRole
    @PatchMapping("/admin/hashtags/update/{id}")
    public ResponseEntity<HashtagResponseDTO> updateHashtag(@PathVariable Long id, @RequestBody HashtagRequestDTO hashtagRequestDTO){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(hashtagService.updateHashtag(id, hashtagRequestDTO));
    }

    //해시태그 삭제
    @AdminRole
    @DeleteMapping("/admin/hashtags/delete/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable Long id){

        boolean isDelete = hashtagService.deleteHashtag(id);

        //삭제 성공
        if(isDelete){
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        //복구 성공
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
