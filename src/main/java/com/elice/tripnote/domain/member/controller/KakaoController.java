package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "user", description = "사용자 API")
@RequestMapping("/api/member")
public class KakaoController implements SwaggerKakaoController {
    private final KakaoService kakaoService;


//    /**
//     * 카카오 로그인 api
//     *
//     * @return
//     * @throws IOException
//     */
//    @Override
//    @GetMapping("/kakao/login")
//    public ResponseEntity<Void> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
//        String accessToken = kakaoService.getAccessToken(code);
//        System.out.println("------------------------- kakao access token: " + accessToken + " -------------------------");
//        kakaoService.kakaoLogin(accessToken);
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 카카오 로그아웃
//     *
//     * @return 로그아웃 유저 id
//     * @throws IOException
//     */
//    @Override
//    @GetMapping("/kakao/logout")
//    public ResponseEntity<Long> kakaoLogout(HttpServletResponse response) throws IOException {
//
//        //TODO: jwt 토큰을 통해서 email 가져오기
//        String email = "";
//        Long kakaoId = kakaoService.logout(email);
//
//        log.info("로그아웃이 완료되었습니다.");
//        return ResponseEntity.ok(kakaoId);
//    }
//
//    /**
//     * 회원 탈퇴(카카오 연결 끊기)
//     * @param response
//     * @return 탈퇴 유저 id
//     * @throws IOException
//     */
//    @Override
//    @GetMapping("/kakao/unlink")
//    public ResponseEntity<Long> kakaoUnlink(HttpServletResponse response) throws IOException {
//
//        //jwt 토큰으로 로그아웃할 유저 아이디 받아오기
////            int userId = jwtService.getUserIdx();
//
//        //TODO: jwt 토큰을 통해서 email 가져오기
//        String email = "";
//
//        //유저 아이디로 카카오 아이디 받아오기
//        Long kakaoId = kakaoService.unlink(email);
//        log.info("회원 탈퇴가 완료되었습니다.");
//        return ResponseEntity.ok(kakaoId);
//
//    }
//
//    /**
//     * 회원 탈퇴(앱이 아닌 경로로 연결 끊기)
//     * @param kakaoId
//     * @param referrerType
//     * @param authorizationHeader
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    @Override
//    @GetMapping("/kakao/disconnect")
//    public ResponseEntity<Long> kakaoDisconnect(
//            @RequestParam("user_id") String kakaoId,
//            @RequestParam("referrer_type") String referrerType,
//            @RequestHeader("Authorization") String authorizationHeader,
//            HttpServletResponse response) throws IOException {
//
////            System.out.println("카카오 헤더: " + authorizationHeader);
////            System.out.println("카카오 유저 아이디: " + kakaoId);
////            System.out.println("referrer type: " + referrerType);
//        log.info("탈퇴하는 user id: {}", kakaoId);
//
//        Long logout_kakaoId = kakaoService.disconnect(Long.parseLong(kakaoId));
//        log.info("회원 탈퇴가 완료되었습니다.");
//        return ResponseEntity.ok(logout_kakaoId);
//
//    }


}
