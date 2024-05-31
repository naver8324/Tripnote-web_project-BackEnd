package com.elice.tripnote.domain.spot.controller;

import com.elice.tripnote.domain.spot.dto.SpotDTO;
import com.elice.tripnote.domain.spot.entity.Spot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;


@Tag(name = "Spot API", description = "여행지 관련 api입니다.")
public interface SwaggerSpotController {
    @Operation(summary = "여행지 조회", description = "지역 혹은 여행지명으로 특정 여행지 조회합니다.")
    @Parameters({
            @Parameter(name = "region", description = "서울특별시, 제주특별자치도, 경기도 등 특별시, 광역시, 도 등 지역 검색"),
            @Parameter(name = "location", description = "에버랜드, 롯데월드 등 특정 여행지명 검색")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 지역 혹은 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<?> getSpots(String region, String location);


    @Operation(summary = "DB에 저장된 여행지 조회", description = "기본키를 통해 여행지를 조회합니다.")
    @Parameters({
            @Parameter(name = "id", description = "Spot 테이블의 기본키")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<Spot> getSpotById(Long id);

    @Operation(summary = "DB에 여행지 추가", description = "여행지를 추가합니다.")
    @Parameters({
            @Parameter(name = "spotDTO", description = "여행지 생성을 위한 여행지 이름, 좋아요 수, 이미지 파일, 지역명")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<Spot> add(SpotDTO spotDTO);

    @Operation(summary = "특정 여행지 좋아요 증가", description = "여행지의 좋아요를 증가합니다.")
    @Parameters({
            @Parameter(name = "location", description = "특정 여행지명")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<Void> increaseLike(String location);

    @Operation(summary = "특정 여행지 좋아요 감소", description = "여행지의 좋아요를 감소합니다.")
    @Parameters({
            @Parameter(name = "location", description = "특정 여행지명")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<Void> decreaseLike(String location);
}
