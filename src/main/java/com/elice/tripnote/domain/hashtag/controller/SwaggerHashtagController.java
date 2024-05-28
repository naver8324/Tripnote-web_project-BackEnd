package com.elice.tripnote.domain.hashtag.controller;

import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Hashtag API", description = "해시태그 관련 api입니다.")
public interface SwaggerHashtagController {

    @Operation(summary = "해시태그 조회", description = "도시 또는 도시가 아닌 해시태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    ResponseEntity<List<HashtagResponseDTO>> getHashtagsByIsCityTrue(@RequestParam(name = "isCity") boolean isCity,
                                                                     @RequestParam(name = "isDelete") boolean isDelete);

    @Operation(summary = "해시태그 생성", description = "해시태그를 생성합니다.")
    @Parameters({
            @Parameter(name = "HashtagRequestDTO", description = "해시태그 내용, 도시유무")
    })
    @ApiResponse(responseCode = "200", description = "성공")
    ResponseEntity<HashtagResponseDTO> createHashtag(@RequestBody HashtagRequestDTO hashtagRequestDTO);

    @Operation(summary = "해시태그 수정", description = "해시태그를 수정합니다.")
    @Parameters({
            @Parameter(name = "id", description = "수정하려는 해시태그 id"),
            @Parameter(name = "HashtagRequestDTO", description = "해시태그 내용, 도시유무")
    })
    @ApiResponse(responseCode = "200", description = "성공")
    ResponseEntity<HashtagResponseDTO> updateHashtag(@PathVariable Long id, @RequestBody HashtagRequestDTO hashtagRequestDTO);

    @Operation(summary = "해시태그 삭제", description = "해시태그를 삭제합니다.")
    @Parameters({
            @Parameter(name = "id", description = "삭제하려는 해시태그 id")
    })
    @ApiResponse(responseCode = "200", description = "성공")
    ResponseEntity<Void> deleteHashtag(@PathVariable Long id);
}
