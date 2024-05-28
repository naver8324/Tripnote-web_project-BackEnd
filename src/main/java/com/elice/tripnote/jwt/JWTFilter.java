package com.elice.tripnote.jwt;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberDetailsDTO;
import com.elice.tripnote.domain.member.entity.Status;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.error("Token is null or does not start with 'Bearer '");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        log.info("Token: " + token);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {
            log.error("Token is expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
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
        }else if(Objects.equals(role, "ROLE_ADMIN")){ // 관리자일 때
            // 아직 미구현
            log.info("admin - in");
        }



        filterChain.doFilter(request, response);
    }
}
