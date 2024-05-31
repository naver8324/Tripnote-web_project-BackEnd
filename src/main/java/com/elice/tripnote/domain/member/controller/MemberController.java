package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements SwaggerMemberController {

    private final MemberService memberService;

//    @GetMapping("/admin")
//    public

    @GetMapping("/test1")
    public String test2(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return email + " | completed test1.";
    }

    // 회원가입
    @Override
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody MemberRequestDTO memberRequestDTO) {
        memberService.signup(memberRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @GetMapping("/{email}")
    public ResponseEntity<Member> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(memberService.getMemberByEmail(email));
    }

    // 이메일 중복검사 (이메일이 이미 존재하면 true 반환, 사용가능하면 false 반환)
    @Override
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        return ResponseEntity.ok().body(memberService.checkEmailDuplicate(email));
    }

    // 닉네임 중복검사 (닉네임이 이미 존재하면 true 반환, 사용가능하면 false 반환)
    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        return ResponseEntity.ok().body(memberService.checkNicknameDuplicate(nickname));
    }

    // (로그인중) 닉네임 변경
    @Override
    @PatchMapping("/update-nickname")
    public ResponseEntity<Void> updateNickname(@RequestHeader("Authorization") String jwt, @RequestParam String newNickname) {
        memberService.updateNickname(newNickname);
        return ResponseEntity.ok().build();
    }

    // (로그인중) 비밀번호 변경 (비밀번호는 노출을 피해야 하기 때문에 RequestBody 형식으로 보냄)
    @Override
    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestHeader("Authorization") String jwt, @RequestBody String newPassword) {
        memberService.updatePassword(newPassword);
        return ResponseEntity.ok().build();
    }

    // (로그인중) 회원 삭제
    @Override
    @DeleteMapping("/delete-member")
    public ResponseEntity<Void> deleteMember(@RequestHeader("Authorization") String jwt) {
        memberService.deleteMember();
        return ResponseEntity.ok().build();
    }
}
