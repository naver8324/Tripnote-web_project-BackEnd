package com.elice.tripnote.domain.member.service;

import com.elice.tripnote.domain.member.entity.KakaoInfoDTO;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.Status;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static com.elice.tripnote.global.exception.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    @Value("${kakao.rest}")
    private String clientKey;

    @Value("${kakao.admin}")
    private String adminKey;

    @Value("${kakao.secret}")
    private String secretKey;
    private final String redirectURI = "http://localhost:8080/api/member/kakao/login";
    //http://localhost:8080
    //http://34.64.39.102:8080

    private final MemberRepository memberRepository;

    public ResponseEntity<Void> getAuthorizationCode(){
        // URL에 쿼리 파라미터 추가
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientKey)
                .queryParam("redirect_uri", redirectURI);

        // 리다이렉트 URL 생성
        URI redirectUrl = uriBuilder.build().toUri();

        // 302 리다이렉트 응답 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return new ResponseEntity<>(httpHeaders, HttpStatus.FOUND);
    }

    //카카오 엑세스 토큰 얻기
    public String getAccessToken(String code) throws IOException {
        String accessToken = "";
        if (code == null) {
            log.info("인증 코드가 존재하지 않습니다.");
            throw new CustomException(ErrorCode.KAKAO_TOKEN_MISSING);
        }

        ResponseEntity<String> response;
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientKey);
            body.add("client_secret", secretKey);
            body.add("code", code);

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new CustomException(ErrorCode.KAKAO_TOKEN_INVALID);
        }

        // HTTP 응답 상태 코드 가져오기
        int responseCode = response.getStatusCodeValue();
        log.info("getAccessToken response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            accessToken = jsonNode.get("access_token").asText();
        } else {
            log.info("요청에 실패하였습니다");
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String error = jsonNode.get("error").asText();
            String error_code = jsonNode.get("error_code").asText();
            String error_description = jsonNode.get("error_description").asText();

            log.error("error: {} ", error);
            log.error("error_code: {} ", error_code);
            log.error("error_Description: {} ", error_description);

            if (error_code.equals("KOE320")) throw new CustomException(ErrorCode.KAKAO_TOKEN_INVALID);
            else if (error_code.equals("KOE303")) throw new CustomException(ErrorCode.REDIRECT_URI_MISMATCH);
            else if (error_code.equals("KOE101")) throw new CustomException(NOT_EXIST_APP_KEY);
        }
        return accessToken;
    }

    @Transactional
    public void kakaoLogin(String accessToken) throws JsonProcessingException {
        KakaoInfoDTO dto = findProfile(accessToken);

        log.info("로그인 하는 유저의 카카오 아이디: {}", dto.getKakaoId());
        Member member = memberRepository.findByOauthIdOrEmail(dto.getKakaoId(), dto.getEmail()).orElseGet(
                () -> {
//            //회원이 아닌 경우
//            //회원가입 진행(이메일, 닉네임 제외 모두)
                    log.info("회원 가입을 진행합니다.");
                    //이메일, 닉네임 중복 검사는 따로
                    Member newMember = Member.builder()
                            .oauthId(dto.getKakaoId())
                            .email(dto.getEmail())
                            .nickname(dto.getNickName())
                            .oauthType("kakao")
                            .status(Status.ACTIVE) // 회원가입시 활동 상태로
                            .build();
                    return memberRepository.save(newMember);
                });
        if(member.getOauthId() == null) {
            log.info("기존 회원 정보와 카카오 회원 정보를 연동시킵니다.");
            member.addKakaoInfo(dto.getKakaoId());
        }
        if (member.getStatus() != Status.ACTIVE) { //탈퇴한 경우
            log.info("유저가 탈퇴한 상태입니다.");
            member.updateStatusACTIVE();
            memberRepository.save(member);
//            throw new CustomException(NO_MEMBER);
        }
        log.info("로그인이 완료되었습니다.");

    }

    // 토큰으로 카카오 API 호출
    @Transactional
    public KakaoInfoDTO findProfile(String accessToken) throws JsonProcessingException {
        ResponseEntity<String> response;

        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new CustomException(ErrorCode.KAKAO_TOKEN_INVALID);
        }

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info("responsesBody: {}", jsonNode);

        //kakao_id, 닉네임, 카카오 이메일 파싱
        Long kakaoId = jsonNode.get("id").asLong();
        String nickName = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        return KakaoInfoDTO.builder()
                .kakaoId(kakaoId)
                .nickName(nickName)
                .email(email)
                .build();
    }


    //카카오 로그아웃
    @Transactional
    public Long logout() throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(NO_MEMBER);
        });
//        Member member = memberRepository.findById(memberId)
        log.info("로그아웃할 유저 id: {}", member.getId());

        Long kakaoId = member.getOauthId();
        String str_kakaoId = String.valueOf(kakaoId);
        ResponseEntity<String> response;

        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + adminKey);

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("target_id_type", "user_id");
            body.add("target_id", str_kakaoId); //로그아웃할 회원의 kakaoId

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            response = rt.exchange(
                    "https://kapi.kakao.com/v1/user/logout",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new CustomException(KAKAO_TOKEN_INVALID);
        }

        // HTTP 응답 상태 코드 가져오기
        int responseCode = response.getStatusCodeValue();
        log.info("getAccessToken response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            kakaoId = jsonNode.get("id").asLong();
//            user.setStatus(Status.LOGGED_OUT);
//            memberRepository.save(user);
        } else {
            log.info("서버 응답 오류");
            throw new CustomException(SERVER_ERROR);
        }

        return kakaoId;
    }

    //카카오 연결끊기
    @Transactional
    public Long unlink() throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new CustomException(NO_MEMBER);
        });

        Long kakaoId = member.getOauthId();
        String str_kakaoId = String.valueOf(kakaoId);

//        User user = memberRepository.getReferenceById(userId);
//        userService.delRefreshToken(user);
//        Long kakaoId = user.getKakaoId();
//
//        String str_kakaoId = String.valueOf(kakaoId);
        System.out.println("탈퇴할 유저의 kakao id: " + str_kakaoId);

        ResponseEntity<String> response;
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "KakaoAK " + adminKey);

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("target_id_type", "user_id");
            body.add("target_id", str_kakaoId); //로그아웃할 회원의 kakaoId

            // HTTP 요청 보내기

            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            response = rt.exchange(
                    "https://kapi.kakao.com/v1/user/unlink",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
        } catch (HttpClientErrorException.Unauthorized ex) {
            throw new CustomException(KAKAO_TOKEN_INVALID);
        }

        // HTTP 응답 상태 코드 가져오기
        int responseCode = response.getStatusCodeValue();
        log.info("getAccessToken response code: {}", responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            kakaoId = jsonNode.get("id").asLong();
//            user.setUserName(null); // 닉네임 중복 검사시, 탈퇴한 회원 닉네임은 안 걸리게
//            member.setStatus(Status.DELETED_BY_USER);
            member.deleteByUser();
            memberRepository.save(member);
        } else {
            log.info("서버 응답 오류");
            throw new CustomException(SERVER_ERROR);
        }

        return kakaoId;
    }

    @Transactional
    public Long disconnect(Long kakaoId) throws IOException {
        Member member = memberRepository.findByOauthId(kakaoId).orElseThrow(() -> {
            throw new CustomException(NO_MEMBER);
        });
//        userService.delRefreshToken(user);
        log.info("탈퇴할 유저의 kakao id: {}", kakaoId);

//        user.setUserName(null); // 닉네임 중복 검사시, 탈퇴한 회원 닉네임은 안 걸리게
        member.deleteByUser();
        memberRepository.save(member);
        return kakaoId;
    }
}
