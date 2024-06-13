package com.elice.tripnote.domain.mail.controller;


import com.elice.tripnote.domain.mail.entity.EmailCheckDTO;
import com.elice.tripnote.domain.mail.entity.EmailRequestDTO;
import com.elice.tripnote.domain.mail.service.MailSendService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
@Slf4j
public class MailController implements SwaggerMailController {
    private final MailSendService mailService;

    // 메일 인증 코드 전송
    @Override
    @PostMapping("/sendmail")
    public ResponseEntity<Void> mailSend(@RequestBody @Valid EmailRequestDTO emailDto) {
        log.info("이메일 인증 이메일 :" + emailDto.getEmail());
        mailService.joinEmail(emailDto.getEmail());
        return ResponseEntity.ok().build();
    }


    // 메일 인증 코드 확인
    @Override
    @PostMapping("/checkmail")
    public ResponseEntity<Boolean> mailCheck(@RequestBody @Valid EmailCheckDTO emailCheckDto){
        return ResponseEntity.ok().body(mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum()));
    }

    // 비밀번호 재설정 요청
    @Override
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid EmailCheckDTO passwordResetRequestDTO) {
        mailService.resetPassword(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getAuthNum());
        return ResponseEntity.ok().build();
    }
}