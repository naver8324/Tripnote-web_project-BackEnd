package com.elice.tripnote.domain.admin.controller;

import com.elice.tripnote.domain.admin.service.AdminService;
import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.global.annotation.AdminRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController implements SwaggerAdminController{

    private final MemberService memberService;
    private final AdminService adminService;

    // 멤버 전체 조회
    @Override
    @AdminRole
    @GetMapping("/members")
    public ResponseEntity<Page<MemberResponseDTO>> getMembers(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok().body(memberService.findMembers(pageable));
    }

    @AdminRole
    @DeleteMapping("/delete-member")
    public ResponseEntity<Void> deleteMember(@RequestParam String email) {
        adminService.deleteMember(email);
        return ResponseEntity.ok().build();
    }
}
