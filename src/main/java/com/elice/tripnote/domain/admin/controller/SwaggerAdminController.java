package com.elice.tripnote.domain.admin.controller;

import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Admin API", description = "관리자 API입니다.")
public interface SwaggerAdminController {


    @Operation(summary = "회원 목록 조회", description = "전체 회원 목록을 페이징하여 조회합니다. (관리자 전용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/admin/members")
    ResponseEntity<Page<MemberResponseDTO>> getMembers(@PageableDefault(size = 10, sort = "id") Pageable pageable);
}
