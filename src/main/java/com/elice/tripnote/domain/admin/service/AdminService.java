package com.elice.tripnote.domain.admin.service;

import com.elice.tripnote.domain.admin.repository.AdminRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final MemberRepository memberRepository;

    // (관리자가) 회원 삭제
    @Transactional
    public void deleteMember(String email) {
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    UsernameNotFoundException ex = new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });

        member.deleteByAdmin();
        memberRepository.save(member); // 상태 및 삭제 시간 업데이트
    }
}
