package com.elice.tripnote.domain.member.service;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(MemberRequestDTO memberRequestDTO) {

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

}
