package com.elice.tripnote.domain.member.service;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberDetailsDTO;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    // 회원가입 서비스
    @Transactional
    public void signup(MemberRequestDTO memberRequestDTO) {

        String email = memberRequestDTO.getEmail();
        String password = memberRequestDTO.getPassword();
        String nickname = memberRequestDTO.getNickname();

        Boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            log.error("이미 존재하는 멤버");
            return;
        }

        log.info("DTO PW : " + memberRequestDTO.getPassword());

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .build();

        memberRepository.save(member);
    }


    // 이메일로 멤버 조회 서비스
    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email));
    }


    // 이메일 중복 체크 서비스
    @Transactional(readOnly = true)
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }


    // 이메일로 멤버 ID 조회 서비스
    @Transactional(readOnly = true)
    public Long getMemberIdByEmail(String email) {
        return memberRepository.findIdByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저 ID를 찾을 수 없습니다. 이메일: " + email));
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> memberData = memberRepository.findByEmail(email);

        return memberData.map(MemberDetailsDTO::new)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email));
    }
}
