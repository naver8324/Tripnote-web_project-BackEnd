package com.elice.tripnote.global.jwt;

import com.elice.tripnote.domain.member.entity.MemberDetailsDTO;
import com.elice.tripnote.domain.member.entity.Status;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 파싱
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(body, new TypeReference<Map<String, String>>() {});

            String email = loginData.get("email");
            String loginId = loginData.get("loginId");
            String password = loginData.get("password");

            log.info("email : " + email);
            log.info("loginId : " + loginId);
            log.info("password : " + password);

            // password 가 null 값이 소셜 로그인 된 이메일로 구분
            if (password == null){
                throw new CustomException(ErrorCode.SOCIAL_LOGIN_EMAIL);
            }

            // 스프링 시큐리티에서 email(또는 loginId)과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken;

            if (email != null) {
                // 사용자 이메일로 로그인
                authToken = new UsernamePasswordAuthenticationToken(email, password);
            } else if (loginId != null) {
                // 관리자 로그인 ID로 로그인
                authToken = new UsernamePasswordAuthenticationToken(loginId, password);
            } else {
                throw new AuthenticationServiceException("Email or LoginId must be provided");
            }

            // token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse authentication request body", e);
        }
    }


    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        //UserDetailsS
        MemberDetailsDTO memberDetailsDTO = (MemberDetailsDTO) authentication.getPrincipal();

        String email = memberDetailsDTO.getUsername();
        log.info("email : "+ email);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        // 회원 상태값 확인
        if (memberDetailsDTO.getStatus() != Status.ACTIVE) {
            if(memberDetailsDTO.getStatus() == Status.DELETED_BY_ADMIN){
                throw new CustomException(ErrorCode.DELETED_BY_ADMIN);
            }else if(memberDetailsDTO.getStatus() == Status.DELETED_BY_USER){
                throw new CustomException(ErrorCode.DELETED_BY_USER);
            }
        }


        String token = jwtUtil.createJwt(email, role, 1000*60*60*10L); //60 * 60 * 10 = 10시간
        log.info("JWT 토큰: {}", token);

        response.addHeader("Authorization", "Bearer " + token);
        log.info("login success");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        String errorMessage = "로그인에 실패했습니다. ";

        if (failed instanceof AuthenticationServiceException) {
            errorMessage += "인증 요청 본문을 파싱하는 데 실패했습니다.";
        } else {
            errorMessage += "유효하지 않은 이메일 또는 비밀번호입니다.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.error("Login failed: {}", errorMessage);
        // 클라이언트에게 실패 이유를 반환합니다.
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print("{\"error\":\"" + errorMessage + "\"}");
        } catch (IOException e) {
            log.error("Failed to send error message to client", e);
        }
    }
}