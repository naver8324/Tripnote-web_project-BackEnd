package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.RecommendedRouteResponseDTO;
import com.elice.tripnote.domain.route.entity.SaveRequestDTO;
import com.elice.tripnote.domain.route.entity.SpotResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "route API", description = "경로 관련 api입니다.")
public interface SwaggerRouteController {
    @Operation(summary = "경로 생성", description = "경로를 추가합니다.")
    @Parameters({
            @Parameter(name = "requestDto", required = true, description = "총 경비, 여행지 id 리스트, 해시태그 id 리스트")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 member ID가 존재하지 않습니다."),
    })
    ResponseEntity<Long> save(SaveRequestDTO requestDto);

    @Operation(summary = "경로 비공개", description = "경로를 비공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", required = true, description = "비공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPrivate(Long routeId);

    @Operation(summary = "경로 공개", description = "경로를 공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", required = true, description = "공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPublic(Long routeId);

    @Operation(summary = "경로 삭제", description = "경로를 삭제합니다.")
    @Parameters({
            @Parameter(name = "routeId", required = true, description = "삭제하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> deleteRoute(Long routeId);

    @Operation(summary = "특정 지역의 경로 리턴", description = "지역을 입력받아서서 해당 지역 안을 여행하는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "region", required = true, description = "지역\n만약, 여러 지역에 걸쳐있는 경로를 찾고 싶다면 뛰어쓰기 없이 '여러지역'이라는 값을 넣으면 된다.\n" +
                    "특별시/광역시 - 서울특별시, 인천광역시, 부산광역시, 대구광역시, 울산광역시, 광주광역시, 대전광역시, 세종특별자치시\n" +
                    "도단위 - 경기도, 강원특별자치도, 충청북도, 충청남도, 경상북도, 경상남도, 전라북도, 전라남도, 제주특별자치도"),
            @Parameter(name = "hashtags", required = false, description = "해시태그 리스트"),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRegion(String region, List<Long> hashtags);

    @Operation(summary = "경로에 포함된 여행지 리스트 리턴", description = "특정 경로 id를 이용해서 해당 경로에 포함된 여행지들을 리스트로 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "routeId", required = true, description = "경로 번호"),
    })
    ResponseEntity<List<SpotResponseDTO>> getSpots(Long integratedRouteId);

//    @Operation(summary="경로의 좋아요 수, 북마크 수 리턴", description= "특정 경로 id를 이용해서 해당 경로의 좋아요 수, 북마크 수를 반환받을 수 있다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공하였습니다.",  content = @Content(mediaType = "application/json")),
//    })
//    @Parameters(value = {
//            @Parameter(name="routeId", description = "경로 번호"),
//    })
//    ResponseEntity<LikeBookmarkResponseDTO> getLikeBookmark(Long integratedRouteId);

    @Operation(summary = "여행지를 포함한 경로 리턴", description = "특정 여행지 id를 이용해서 해당 여행지를 지나가는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "hashtags", required = false, description = "해시태그 리스트"),
            @Parameter(name = "spots", required = true, description = "여행지 리스트"),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpot(List<Long> hashtags, List<Long> spots);
}
