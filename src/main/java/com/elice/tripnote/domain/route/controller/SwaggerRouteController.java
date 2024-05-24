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
            @Parameter(name = "requestDto", description = "경로를 만드는 유저 id, 여행지 id 리스트, 여행지별 경비 리스트, 해시태그 id 리스트")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Long.class))}),
    })
    ResponseEntity<Long> save(SaveRequestDto requestDto);
}
