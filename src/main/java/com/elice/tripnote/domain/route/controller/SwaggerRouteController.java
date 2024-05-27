package com.elice.tripnote.domain.route.controller;

import com.elice.tripnote.domain.route.dto.SaveRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "route API", description = "경로 관련 api입니다.")
public interface SwaggerRouteController {
    @Operation(summary = "경로 생성", description = "경로를 추가합니다.")
    @Parameters({
            @Parameter(name = "requestDto", description = "경로를 만드는 유저 id, 총 경비, 여행지 id 리스트, 해시태그 id 리스트")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 유저 ID 또는 Order Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> save(SaveRequestDto requestDto);

    @Operation(summary = "경로 비공개", description = "경로를 비공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "비공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPrivate(Long routeId);

    @Operation(summary = "경로 공개", description = "경로를 공개 상태로 변경합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "공개하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> setRouteToPublic(Long routeId);

    @Operation(summary = "경로 삭제", description = "경로를 삭제합니다.")
    @Parameters({
            @Parameter(name = "routeId", description = "삭제하려는 경로의 id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "404", description = "해당 Route Id가 존재하지 않습니다."),
    })
    ResponseEntity<Long> deleteRoute(Long routeId);
}
