package com.elice.tripnote.domain.member.service;

import com.elice.tripnote.domain.admin.entity.Admin;
import com.elice.tripnote.domain.admin.repository.AdminRepository;
import com.elice.tripnote.domain.member.entity.*;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    // 회원가입 서비스
    @Transactional
    public void signup(MemberRequestDTO memberRequestDTO) {

        String email = memberRequestDTO.getEmail();
        String password = memberRequestDTO.getPassword();
        log.info("비번 : "+bCryptPasswordEncoder.encode(password));
        String nickname = memberRequestDTO.getNickname();

        // 이메일 중복 검사
        if (memberRepository.existsByEmail(email)) {
            log.error("에러 발생: {}", ErrorCode.DUPLICATE_EMAIL);
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 닉네임 중복 검사
        if (memberRepository.existsByNickname(nickname)) {
            log.error("에러 발생: {}", ErrorCode.DUPLICATE_NICKNAME);
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .status(Status.ACTIVE) // 회원가입시 활동 상태로
                .build();

        memberRepository.save(member);
    }

    // 이메일로 멤버 조회 서비스
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberResponseDTOByEmail(String email) {
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    UsernameNotFoundException ex = new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });

        return member.toDto();
    }


    // 이메일 중복 체크 & 소셜 이메일인지 확인하는 서비스
    @Transactional(readOnly = true)
    public String checkEmailStatus(String email) {
        Optional<Long> oauthId = memberRepository.findOauthIdByEmail(email);
        if (oauthId.isPresent() && oauthId.get() != null) {
            return "social";
        } else if (memberRepository.existsByEmail(email)) {
            return "true";
        }
        return "false";
    }


    // 닉네임 중복 체크 서비스
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }


    // 이메일로 멤버 ID 조회 서비스
    @Transactional(readOnly = true)
    public Long getMemberIdByEmail(String email) {
        return memberRepository.findIdByEmail(email)
                .orElseThrow(() -> {
                    UsernameNotFoundException ex = new UsernameNotFoundException("해당 이메일로 유저 ID를 찾을 수 없습니다. 이메일: " + email);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });
    }

    // 로그인 로직
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            return memberOptional.map(MemberDetailsDTO::new).get();
        } else {
            Optional<Admin> adminOptional = adminRepository.findByLoginId(email);
            if (adminOptional.isPresent()) {
                return adminOptional.map(MemberDetailsDTO::new).get();
            } else {
                throw new UsernameNotFoundException("해당 이메일(또는 아이디)로 사용자를 찾을 수 없습니다. 이메일(또는 아이디): " + email);
            }
        }
    }



    // 프로필 업데이트 서비스
    @Transactional
    public void updateProfile(ProfileUpdateDTO profileUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            if (profileUpdateDTO.getNewNickname() != null) {
                // 바꿀 닉네임 중복 검사
                if (memberRepository.existsByNickname(profileUpdateDTO.getNewNickname())) {
                    log.error("에러 발생: {}", ErrorCode.DUPLICATE_NICKNAME);
                    throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
                }
                member.updateNickname(profileUpdateDTO.getNewNickname());
            }

            if (profileUpdateDTO.getNewPassword() != null) {
                member.updatePassword(bCryptPasswordEncoder.encode(profileUpdateDTO.getNewPassword()));
            }

            memberRepository.save(member);
        } else {
            throw new CustomException(ErrorCode.NO_MEMBER);
        }
    }

    // (회원이) 회원 삭제
    @Transactional
    public void deleteMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    UsernameNotFoundException ex = new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });

        member.deleteByUser();
        memberRepository.save(member); // 상태 및 삭제 시간 업데이트
    }

    // 비밀번호 검증 서비스
    @Transactional
    public boolean validatePassword(PasswordDTO validatePasswordDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String password = memberRepository.findPasswordByEmail(email);

        // BCryptPasswordEncoder의 matches 메소드를 사용하여 비밀번호 검증
        boolean isPasswordMatch = bCryptPasswordEncoder.matches(validatePasswordDTO.getPassword(), password);
        log.info("Password Match: " + isPasswordMatch);

        return isPasswordMatch;
    }

    // 전체 멤버 조회 서비스
    //public Page<MemberResponseDTO> findMembers(Pageable pageable) {
    //    Page<Member> members = memberRepository.findAll(pageable);
    //    return members.map(Member::toDto);
    //}


    // 로그인중인 멤버 정보 조회 서비스
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberResponseDTO() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member =  memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    UsernameNotFoundException ex = new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다. 이메일: " + email);
                    log.error("에러 발생: {}", ex.getMessage(), ex);
                    return ex;
                });

        return member.toDto();
    }
}
