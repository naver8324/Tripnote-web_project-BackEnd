package com.elice.tripnote.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface SwaggerKakaoController {
//    @Operation(summary = "카카오 로그인 API")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4001", description = "JWT 토큰을 주세요!"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4002", description = "JWT 토큰 만료"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4010", description = "DB에 존재하지 않는 회원"),
//    })
//    @Parameters({
//            @Parameter(name = "Authorization", description = "카카오에서 받아오는 엑세스 토큰을 넣어주세요.", in = ParameterIn.HEADER)
//    })
//    ResponseEntity<Void> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException;
//
//    @Operation(summary = "회원 로그아웃 API")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4001", description = "JWT 토큰을 주세요!"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4002", description = "JWT 토큰 만료"),
//    })
//    ResponseEntity<Long> kakaoLogout(HttpServletResponse response) throws IOException;
//
//    @Operation(summary = "회원 탈퇴 API")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4001", description = "JWT 토큰을 주세요!"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "JWT4002", description = "JWT 토큰 만료"),
//    })
//    ResponseEntity<Long> kakaoUnlink(HttpServletResponse response) throws IOException;
//
//    @Operation(summary = "회원 탈퇴 API - 사용자가 앱이 아닌 카카오 계정 관리 페이지나 고객센터에서 연결 끊기를 진행하는 경우")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//    })
//    ResponseEntity<Long> kakaoDisconnect(String kakaoId, String referrerType, String authorizationHeader, HttpServletResponse response) throws IOException;
}
