package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.entity.*;
import com.elice.tripnote.global.entity.PageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "route API", description = "경로 관련 api입니다.")
public interface SwaggerRouteController {
    @Operation(summary = "경로 생성", description = "경로를 추가합니다.")
//    @Parameters({
//            @Parameter(name = "requestDto", required = true, description = "총 경비(없어도 됨), 경로 이름, 여행지 id 리스트, 해시태그 id 리스트(없어도 됨)")
//    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 memberID 또는 spot ID가 존재하지 않습니다."),
    })
    ResponseEntity<Long> save(SaveRequestDTO requestDto);

//    @Operation(summary = "경로 공개/비공개", description = "경로가 현재 공개 상태라면 비공개로, 현재 비공개 상태라면 공개 상태로 변경합니다.")
//    @Parameters({
//            @Parameter(name = "routeId", required = true, description = "공개 여부를 수정하려는 경로의 id")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공",
//                    content = {@Content(schema = @Schema(implementation = Long.class))}),
//            @ApiResponse(responseCode = "403", description = "해당 route를 비공개할 수 있는 권한이 없습니다."),
//            @ApiResponse(responseCode = "404", description = "해당 member ID 또는 Route Id가 존재하지 않습니다."),
//    })
//    ResponseEntity<Long> setRouteStatus(Long routeId);

    @Operation(summary = "경로 삭제", description = "경로를 삭제합니다.")
    @Parameters({
            @Parameter(name = "routeId", required = true, description = "삭제하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "403", description = "해당 route를 삭제할 수 있는 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 member ID 또는Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> deleteRoute(Long routeId);

    @Operation(summary = "경로 정보 조회", description = "특정 경로의 경로 정보를 조회합니다.")
    @Parameters({
            @Parameter(name = "routeId", required = true, description = "조회하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 member ID 또는Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<RecommendedRouteResponseDTO> getRouteInfo(@PathVariable("routeId") Long routeId);

    @Operation(summary = "지역 기반 경로 추천(회원)", description = "지역을 입력받아서 해당 지역 안을 여행하는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID가 존재하지 않습니다."),
    })
    @Parameters(value = {
            @Parameter(name = "region", required = true, description = "지역\n만약, 여러 지역에 걸쳐있는 경로를 찾고 싶다면 'mix'라는 값을 넣으면 된다." + "\n" +
                    "특별시/광역시 - seoul(서울), incheon(인천), busan(부산), daegu(대구), ulsan(울산), gwangju(광주), daejeon(대전), sejong(세종시)" + "\n" +
                    "도단위 - gyeonggi(경기도), gangwon(강원도), chungbuk(충청북도), chungnam(충청남도), gyeongbuk(경상북도), gyeongnam(경상남도), jeonbuk(전라북도), jeonnam(전라남도), jeju(제주도)"
            ),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRegion(String region);

    @Operation(summary = "지역 기반 경로 추천(비회원)", description = "지역을 입력받아서 해당 지역 안을 여행하는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "region", required = true, description = "지역\n만약, 여러 지역에 걸쳐있는 경로를 찾고 싶다면 'mix'라는 값을 넣으면 된다." + "\n" +
                    "특별시/광역시 - seoul(서울), incheon(인천), busan(부산), daegu(대구), ulsan(울산), gwangju(광주), daejeon(대전), sejong(세종시)" + "\n" +
                    "도단위 - gyeonggi(경기도), gangwon(강원도), chungbuk(충청북도), chungnam(충청남도), gyeongbuk(경상북도), gyeongnam(경상남도), jeonbuk(전라북도), jeonnam(전라남도), jeju(제주도)"
            ),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRegionGuest(String region);

    @Operation(summary = "여행지 기반 경로 추천(회원)", description = "특정 여행지 id를 이용해서 해당 여행지를 지나가는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID가 존재하지 않습니다."),
    })
    @Parameters(value = {
            @Parameter(name = "spots", required = true, description = "여행지 리스트"),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpot(List<Long> spots);


    @Operation(summary = "여행지 기반 경로 추천(비회원)", description = "특정 여행지 id를 이용해서 해당 여행지를 지나가는 경로 id 리스트를 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "spots", required = true, description = "여행지 리스트"),
    })
    ResponseEntity<List<RecommendedRouteResponseDTO>> getRoutesThroughSpotGuest(List<Long> spots);

    @Operation(summary = "특정 경로에 포함된 여행지 리스트를 알 수 있음(jwt 토큰 필요없음)", description = "특정 경로 id를 이용해서 해당 경로에 포함된 여행지들을 리스트로 반환받을 수 있다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters(value = {
            @Parameter(name = "routeId", required = true, description = "경로 번호"),
    })
    ResponseEntity<List<SpotResponseDTO>> getSpots(Long routeId);

    @Operation(summary = "좋아요 추가/취소", description = "좋아요가 눌리지 않은 상태에서 이 api를 호출하면 -> 좋아요 추가" + "\n" +
            "좋아요가 눌린 상태에서 이 api 호출 -> 좋아요 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID 또는 입력된 경로 id와 연결된 like_bookmark_period 객체가 존재하지 않습니다."),
    })
    @Parameters(value = {
            @Parameter(name = "integratedRouteId", required = true, description = "좋아요를 누르고 싶은 경로 id"),
    })
    ResponseEntity<Void> addOrRemoveLike(Long integratedRouteId);

    @Operation(summary = "북마크 추가/취소", description = "북마크가 눌리지 않은 상태에서 이 api를 호출하면 -> 북마크 추가" + "\n" +
            "북마크가 눌린 상태에서 이 api 호출 -> 북마크 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID 또는 입력된 경로 id와 연결된 like_bookmark_period 객체가 존재하지 않습니다."),
    })
    @Parameters(value = {
            @Parameter(name = "integratedRouteId", required = true, description = "북마크를 누르고 싶은 경로 id"),
    })
    ResponseEntity<Void> addOrRemoveBookmark(Long integratedRouteId);

    @Operation(summary = "내가 북마크 누른 경로 리스트", description = "자신이 누른 북마크 경로를 반환합니다." + "\n" +
            " query string으로 size=3 값 넣어주셔야 3개씩 나옵니다!" + "\n" +
            "page=1이 가장 첫페이지입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID가 존재하지 않습니다."),
    })
//    @Parameters(value = {
//            @Parameter(name = "page", required = false, description = "원하는 page, 첫번째 페이지를 보고 싶다면 0을 입력하면 된다.\n값을 넣지 않는다면 기본값으로 0이 들어간다"),
//    })
    ResponseEntity<Page<RouteDetailResponseDTO>> findBookmark(PageRequestDTO pageRequestDTO);

    @Operation(summary = "내가 생성한 경로 리스트", description = "자신이 만든 경로를 반환합니다." + "\n" +
            " query string으로 size=3 값 넣어주셔야 3개씩 나옵니다!" + "\n" +
            "page=1이 가장 첫페이지입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 member ID가 존재하지 않습니다."),
    })
    ResponseEntity<Page<RouteDetailResponseDTO>> findMyRoute(PageRequestDTO pageRequestDTO);

    @Operation(summary = "경로 이름 수정", description = "경로의 이름을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "403", description = "해당 경로의 이름을 수정할 수 있는 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당 memberID가 존재하지 않습니다."),
    })
    ResponseEntity<Void> updateName(@RequestBody UpdateRouteNameRequestDTO requestDto);
}
