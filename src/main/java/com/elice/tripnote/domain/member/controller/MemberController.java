package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements SwaggerMemberController {

    private final MemberService memberService;


    @GetMapping("/test")
    public String test(){
        return "completed test.";
    }

    @GetMapping("/test1")
    public String test2(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return email + " | completed test1.";
    }

    // 회원가입 테스트중
    @Override
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody MemberRequestDTO memberRequestDTO) {
        log.info("닉네임 : " + memberRequestDTO.getNickname());
        memberService.signup(memberRequestDTO);
        return ResponseEntity.ok("signup completed");
    }

    @Override
    @GetMapping("/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        Member member = memberService.getMemberByEmail(email);
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 이메일 중복검사 (이메일이 이미 존재하면 true, 사용가능하면 false)
    @Override
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicate = memberService.checkEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }
}
