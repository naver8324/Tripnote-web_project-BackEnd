package com.elice.tripnote.domain.hashtag.controller;

import com.elice.tripnote.domain.hashtag.entity.HashtagDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagRequestDTO;
import com.elice.tripnote.domain.hashtag.entity.HashtagResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Hashtag API", description = "해시태그 관련 api입니다.")
public interface SwaggerHashtagController {

    @Operation(summary = "해시태그 전체 조회", description = "해시태그의 모든 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    ResponseEntity<Page<HashtagDTO>> getHashtags(PageRequestDTO pageRequestDTO);

    @Operation(summary = "해시태그 조회", description = "도시 또는 도시가 아닌 해시태그를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @Parameters(value = {
            @Parameter(name="isCity", description = "지역여부", example = "true, false")
    })
    ResponseEntity<List<HashtagResponseDTO>> getHashtagsByIsCityTrue(boolean isCity);

    @Operation(summary = "해시태그 생성", description = "해시태그를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "409", description = "중복되는 해시태그명입니다."),
    })    ResponseEntity<HashtagResponseDTO> createHashtag( HashtagRequestDTO hashtagRequestDTO);

    @Operation(summary = "해시태그 수정", description = "해시태그를 수정합니다.")
    @Parameters(value = {
            @Parameter(name = "id", description = "수정하려는 해시태그 id"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "409", description = "중복되는 해시태그명입니다."),
    })
    ResponseEntity<HashtagResponseDTO> updateHashtag(Long id, HashtagRequestDTO hashtagRequestDTO);

    @Operation(summary = "해시태그 삭제", description = "해시태그를 삭제합니다.")
    @Parameters(value = {
            @Parameter(name = "id", description = "삭제하려는 해시태그 id", example = "1234")
    })
    @ApiResponse(responseCode = "204", description = "해시태그 삭제에 성공하였습니다.")
    @ApiResponse(responseCode = "200", description = "해시태그 복구에 성공하였습니다.")
    ResponseEntity<Void> deleteHashtag(Long id);
}
