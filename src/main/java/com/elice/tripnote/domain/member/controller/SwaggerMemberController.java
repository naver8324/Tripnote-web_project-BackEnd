package com.elice.tripnote.domain.member.controller;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "member API")
public interface SwaggerMemberController {

    @Operation(summary = "회원 가입")
    @PostMapping("/signup")
    ResponseEntity<Void> signup(@RequestBody MemberRequestDTO memberRequestDTO);

    @Operation(summary = "이메일로 회원 조회")
    @GetMapping("/{email}")
    ResponseEntity<Member> getMemberByEmail(@PathVariable String email);

    @Operation(summary = "이메일 중복 확인")
    @GetMapping("/check-email")
    ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email);

    @Operation(summary = "닉네임 중복 확인")
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname);

}
