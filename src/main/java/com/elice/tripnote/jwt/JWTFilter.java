package com.elice.tripnote.jwt;

import com.elice.tripnote.domain.admin.entity.Admin;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberDetailsDTO;
import com.elice.tripnote.domain.member.entity.Status;
import com.elice.tripnote.global.exception.ErrorCode;
import com.elice.tripnote.global.exception.JwtTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        // JWT Filter 가 필요없는 경우 JWT 필터를 통과시킴
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/member/signup") || requestURI.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }


        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.error("토큰이 null이거나 'Bearer '로 시작하지 않습니다");
//            throw new JwtTokenException(ErrorCode.TOKEN_MISSING_OR_INVALID);
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        log.info("Token: " + token);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.error("토큰이 만료되었습니다");
//            throw new JwtTokenException(ErrorCode.TOKEN_EXPIRED);
            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 username과 role 획득
        String email = jwtUtil.getEmail(token);
        String role = jwtUtil.getRole(token);
        log.info("jwtUtil.getRole : "+ role);

        if(Objects.equals(role, "ROLE_MEMBER")){
            log.info("member - in");
            // Member 객체 생성 및 값 설정
            Member member = Member.builder()
                    .email(email)
                    .password("temppassword") // JWT에서 비밀번호는 사용되지 않으므로 임시 비밀번호 설정
                    .status(Status.ACTIVE) // 상태 설정
                    .build();

            // MemberDetailsDTO에 회원 정보 객체 담기
            MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO(member);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetailsDTO, null, memberDetailsDTO.getAuthorities());

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }else if(Objects.equals(role, "ROLE_ADMIN")){
            // 관리자일 때
            log.info("admin - in");
            // 아직 미구현
            Admin admin = Admin.builder()
                    .loginId(email)
                    .password("temppassword") // JWT에서 비밀번호는 사용되지 않으므로 임시 비밀번호 설정
                    .build();

            // MemberDetailsDTO에 회원 정보 객체 담기
            MemberDetailsDTO memberDetailsDTO = new MemberDetailsDTO(admin);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetailsDTO, null, memberDetailsDTO.getAuthorities());

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }else{
            // 유효하지 않은 역할 처리
            log.error("Invalid role: " + role);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid role");
            return; // 조건이 해당되면 메소드 종료 (필수)
        }



        filterChain.doFilter(request, response);
    }
}
