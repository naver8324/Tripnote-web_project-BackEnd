package com.elice.tripnote.global.interceptor;

import com.elice.tripnote.domain.admin.entity.Admin;
import com.elice.tripnote.domain.member.entity.MemberDetailsDTO;
import com.elice.tripnote.domain.member.service.TokenBlacklistService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import com.elice.tripnote.global.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // @MemberRole 어노테이션이 있으면 검증 로직 수행
        if (handlerMethod.hasMethodAnnotation(AdminRole.class)) {

            // JWT 토큰 가져오기
            String token = extractToken(request);

            // 토큰 블랙리스트 검증 (로그아웃된 토큰)
            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                log.error("블랙리스트에 등록된 토큰입니다.");
                throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
            }

            // 토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {
                log.error("토큰이 만료되었습니다");
                throw new CustomException(ErrorCode.TOKEN_EXPIRED);
            }

            // 토큰 검증 및 사용자 인증
            authenticateUser(token, response);
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("토큰이 null이거나 'Bearer '로 시작하지 않습니다");
            throw new CustomException(ErrorCode.TOKEN_MISSING_OR_INVALID);
        }

        return authorization.split(" ")[1];
    }


    private void authenticateUser(String token, HttpServletResponse response) {
        String role = jwtUtil.getRole(token);

        if (Objects.equals(role, "ROLE_ADMIN")) {
            String email = jwtUtil.getEmail(token);

            Admin admin = Admin.builder()
                    .loginId(email)
                    .password("temppassword") // 임시 비밀번호
                    .build();

            // MemberDetailsDTO에 회원 정보 객체 담기
            MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO(admin);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetailsDTO, null, memberDetailsDTO.getAuthorities());

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            log.error("Invalid role: " + role);
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}