package com.elice.tripnote.domain.admin.controller;

import com.elice.tripnote.domain.admin.service.AdminService;
import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.domain.member.service.MemberService;
import com.elice.tripnote.global.annotation.AdminRole;
import com.elice.tripnote.global.entity.PageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class    AdminController implements SwaggerAdminController{

    private final AdminService adminService;
    private final MemberRepository memberRepository;

    // 멤버 전체 조회
    //@Override
    @AdminRole
    @GetMapping("/members")
    public ResponseEntity<Page<MemberResponseDTO>> getMembers(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok().body(memberRepository.customFindAll(pageRequestDTO));
    }

    // 멤버 삭제
    @Override
    @AdminRole
    @DeleteMapping("/delete-member")
    public ResponseEntity<Void> deleteMember(@RequestParam String email) {
        adminService.deleteMember(email);
        return ResponseEntity.ok().build();
    }

    @Override
    @AdminRole
    @DeleteMapping("/restore-member")
    public ResponseEntity<Void> restoreMember(@RequestParam String email) {
        adminService.restoreMember(email);
        return ResponseEntity.ok().build();
    }
}
