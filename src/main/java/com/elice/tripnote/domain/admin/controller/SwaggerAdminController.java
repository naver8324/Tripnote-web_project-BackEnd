package com.elice.tripnote.domain.admin.controller;

import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.global.entity.PageRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Admin API", description = "관리자 API입니다.")
public interface SwaggerAdminController {


    @Operation(summary = "회원 목록 조회", description = "회원 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/admin/members")
    ResponseEntity<Page<MemberResponseDTO>> getMembers(PageRequestDTO pageRequestDTO);

    @Operation(summary = "회원 삭제", description = "이메일을 통해 회원을 삭제합니다.",
            parameters = @Parameter(name = "email", description = "삭제할 회원의 이메일", required = true, schema = @Schema(type = "string")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 삭제에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)"),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당하는 회원을 찾을 수 없습니다.")
    })
    @DeleteMapping("/delete-member")
    ResponseEntity<Void> deleteMember(@RequestParam String email);

    @Operation(summary = "회원 복구", description = "이메일을 통해 회원을 복구합니다.",
            parameters = @Parameter(name = "email", description = "복구할 회원의 이메일", required = true, schema = @Schema(type = "string")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 복구에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)"),
            @ApiResponse(responseCode = "403", description = "관리자 권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당하는 회원을 찾을 수 없습니다.")
    })
    @DeleteMapping("/restore-member")
    ResponseEntity<Void> restoreMember(@RequestParam String email);


}
