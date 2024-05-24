package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements SwaggerMemberController {

    private final MemberService memberService;


    // 회원가입 테스트중
    @PostMapping("/join")
    public String joinProcess(@RequestBody MemberRequestDTO memberRequestDTO) {

        log.info("닉네임 : " + memberRequestDTO.getNickname());
        memberService.joinProcess(memberRequestDTO);

        return "ok";
    }
}
