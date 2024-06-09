package com.elice.tripnote.domain.admin.service;

import com.elice.tripnote.domain.admin.repository.AdminRepository;
import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
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
        Member member = existByEmailCheck(email);

        //이미 삭제된 회원인경우 예외처리
        if(!(member.getDeletedAt() == null)){
            CustomException ex = new CustomException(ErrorCode.MEMBER_ALREADY_DELETED);
            log.error("에러 발생: {}", ex.getMessage(), ex);
            throw ex;
        }

        member.deleteByAdmin();
        memberRepository.save(member); // 상태 및 삭제 시간 업데이트
    }

    // (관리자가) 회원 복구
    @Transactional
    public void restoreMember(String email) {
        Member member = existByEmailCheck(email);

        //이미 복구된 회원인경우 예외처리
        if(member.getDeletedAt() == null){
            CustomException ex = new CustomException(ErrorCode.MEMBER_ALREADY_RESTORED);
            log.error("에러 발생: {}", ex.getMessage(), ex);
            throw ex;
        }

        member.restoreByAdmin();
        memberRepository.save(member); // 상태 및 삭제 시간 업데이트
    }


    //존재하는 이메일인지 체크 체크
    private Member existByEmailCheck(String email){

        boolean isExist = memberRepository.existsByEmail(email);
        //해당 이메일이 이미 존재하는 경우 true 반환 -> 예외 처리
        if(isExist){
            return memberRepository.getByEmail(email);
        }

        CustomException ex = new CustomException(ErrorCode.NO_EMAIL);
        log.error("에러 발생: {}", ex.getMessage(), ex);
        throw ex;
    }
}
