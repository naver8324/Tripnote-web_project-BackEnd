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
//    @Override
//    @PostMapping("/sendmail")
//    public ResponseEntity<Void> mailSend(@RequestBody @Valid EmailRequestDTO emailDto) {
//        log.info("이메일 인증 이메일 :" + emailDto.getEmail());
//        mailService.joinEmail(emailDto.getEmail());
//        return ResponseEntity.ok().build();
//    }

    //TODO: (프론트 테스트 끝나면 위에 코드 주석해제 후 현재 메소드 지우기, joinEmail 메소드 리턴값 지우기)
    // 메일 인증 코드 전송
    @Override
    @PostMapping("/sendmail")
    public ResponseEntity<String> mailSend(@RequestBody @Valid EmailRequestDTO emailDto) {
        log.info("이메일 인증 이메일 :" + emailDto.getEmail());

        return ResponseEntity.ok().body(mailService.joinEmail(emailDto.getEmail()));
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
    public ResponseEntity<String> resetPassword(@RequestBody @Valid EmailCheckDTO passwordResetRequestDTO) {
        String implyPassword = mailService.resetPassword(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getAuthNum());
        return ResponseEntity.ok().body(implyPassword);
//        return ResponseEntity.ok().build();
    }
}