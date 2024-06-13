package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Member API", description = "회원 API입니다.")
public interface SwaggerMemberController {

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입에 성공하였습니다.")
    @PostMapping("/signup")
    ResponseEntity<Void> signup(@RequestBody MemberRequestDTO memberRequestDTO);


    @Operation(summary = "이메일 중복 확인", description = "입력한 이메일이 이미 등록되어 있는지 확인합니다. (이메일이 이미 존재하면 'true' 반환, 사용가능하면 'false' 반환, 소셜 이메일이면 'social' 반환)")
    @ApiResponse(responseCode = "200", description = "이메일 중복 확인에 성공하였습니다.")
    @GetMapping("/check-email")
    ResponseEntity<String> checkEmailDuplicate(@RequestParam @Parameter(description = "이메일 주소", required = true) String email);


    @Operation(summary = "닉네임 중복 확인", description = "입력한 닉네임이 이미 등록되어 있는지 확인합니다. (닉네임이 이미 존재하면 true 반환, 사용가능하면 false 반환)")
    @ApiResponse(responseCode = "200", description = "닉네임 중복 확인에 성공하였습니다.")
    @GetMapping("/check-nickname")
    ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam @Parameter(description = "닉네임", required = true) String nickname);



    @Operation(summary = "프로필 업데이트", description = "회원의 닉네임 및 비밀번호를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 업데이트에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당 이메일을 가진 회원이 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/update-profile")
    ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateDTO profileUpdateDTO);


    @Operation(summary = "회원 삭제", description = "(로그인중) 회원을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 삭제에 성공하였습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.")
    })
    @DeleteMapping("/delete-member")
    ResponseEntity<Void> deleteMember();


    @Operation(summary = "비밀번호 검증", description = "(로그인 중) 회원의 비밀번호를 검증합니다. (비밀번호가 일치하면 true 반환, 일치하지 않으면 false 반환)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 일치합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. (토큰 값이 제대로 전달되었는지 확인이 필요합니다.)", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "해당하는 유저는 존재하지 않습니다.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/validate-password")
    ResponseEntity<Boolean> validatePassword(@RequestBody @Parameter(description = "검증할 비밀번호(json형식으로 key는 password)", required = true) PasswordDTO validatePasswordDTO);




    @Operation(summary = "로그인중인 회원 조회", description = "토큰 기반으로 회원을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "회원 조회에 성공하였습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Member.class)))
    @GetMapping
    ResponseEntity<MemberResponseDTO> getMemberByToken();


    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃에 성공했습니다."),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 에러 발생", content = @Content)
    })
    @PostMapping("/logout")
    ResponseEntity<Void> logout(HttpServletRequest request);



    /*
    카카오 로그인 api
     */
   @Operation(summary = "카카오 로그인 API(kakao login을 위해 필요한 리다이렉트 url string으로 전달)")
   @ApiResponses({
           @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
   })
   ResponseEntity<String> kakao();

   @Operation(summary = "카카오 로그인을 한 코드를 프론트에서 받아온 후 카카오에 토큰 발급 요청 및 받아옴")
   @ApiResponses({
           @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
   })
   @Parameters({
           @Parameter(name = "code", description = "카카오에서 받아오는 코드를 넣어주세요.")
   })
   ResponseEntity<TokenResponseDTO> kakaoLogin(@RequestParam(name = "code") String code);

    @Operation(summary = "회원 로그아웃 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    @Parameters({
            @Parameter(name = "kakaoToken", description = "카카오에서 받아온 토큰을 넣어주세요.")
    })
    ResponseEntity<Long> kakaoLogout(@RequestParam(name = "kakaoToken") String kakaoToken, HttpServletResponse response, HttpServletRequest request) throws IOException;

    @Operation(summary = "회원 탈퇴 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    ResponseEntity<Long> kakaoUnlink(HttpServletResponse response) throws IOException;

    @Operation(summary = "회원 탈퇴 API - 사용자가 앱이 아닌 카카오 계정 관리 페이지나 고객센터에서 연결 끊기를 진행하는 경우")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공"),
    })
    ResponseEntity<Long> kakaoDisconnect(String kakaoId, String referrerType, String authorizationHeader, HttpServletResponse response) throws IOException;





}
