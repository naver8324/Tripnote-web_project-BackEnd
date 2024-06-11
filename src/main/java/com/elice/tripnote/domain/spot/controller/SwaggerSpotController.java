package com.elice.tripnote.domain.spot.controller;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.dto.SpotDetailDTO;
import com.elice.tripnote.domain.spot.dto.SpotRequestDTO;
import com.elice.tripnote.domain.spot.dto.SpotResponseDTO;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


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
    ResponseEntity<?> getSpots(Region region, String location);


    @Operation(summary = "DB에 저장된 여행지 조회", description = "기본키를 통해 여행지를 조회합니다.")
    @Parameters({
            @Parameter(name = "id", description = "Spot 테이블의 기본키")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
    })
    ResponseEntity<SpotDetailDTO> getSpotById(Long id);
//    @Operation(summary = "DB에 저장된 여행지 조회", description = "기본키를 통해 여행지를 조회합니다.")
//    @Parameters({
//            @Parameter(name = "id", description = "Spot 테이블의 기본키")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공",
//                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
//            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
//    })
//    ResponseEntity<SpotDTO> getSpotById(Long id);

    @Operation(summary = "클라이언트 spot 선택", description = "클라이언트가 경로 만들 때 선택한 여행지가 id를 통해 list 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공",
                    content = {@Content(schema = @Schema(implementation = SpotResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "경로 생성 시 적절한 값 생성 실패")
    })
    ResponseEntity<List<Spot>> getSpotForRoute(@RequestBody SpotRequestDTO requestDTO);
//
//    @Operation(summary = "경로 생성 페이지", description = "특정 지역 선택 후 해당 지역 내 여행지 목록 조회")
//    @Parameters({
//            @Parameter(name = "region", description = "서울특별시, 제주특별자치도, 경기도 등 특별시, 광역시, 도 등 지역 검색")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공",
//                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
//            @ApiResponse(responseCode = "404", description = "해당이 존재하지 않습니다.")
//    })
//    ResponseEntity<List<Spot>> createRoute(String region);
//
//    @Operation(summary = "DB에 여행지 추가", description = "여행지를 추가합니다.")
//    @Parameters({
//            @Parameter(name = "spotRequestDTO", description = "여행지 생성을 위한 여행지 이름, 좋아요 수, 이미지 파일, 지역명")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "성공",
//                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
//            @ApiResponse(responseCode = "404", description = "해당 여행지가 존재하지 않습니다.")
//    })
//    ResponseEntity<Spot> add(SpotRequestDTO spotRequestDTO);
//
//
//    @Operation(summary = "좌표 기반 위치 조회", description = "위도, 경도 기반으로 서울, 경기도 등 특정 위치 조회")
//    @Parameters({
//            @Parameter(name = "lat", description = "위도"),
//            @Parameter(name = "lng", description = "경도")
//    })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공",
//                    content = {@Content(schema = @Schema(implementation = Spot.class))}),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 좌표값입니다.")
//    })
//    ResponseEntity<String> getAddressByCoordinates(double lat,double lng);
}
