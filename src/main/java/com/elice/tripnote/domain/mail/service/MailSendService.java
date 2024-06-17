package com.elice.tripnote.domain.mail.service;

import com.elice.tripnote.domain.member.repository.MemberRepository;
import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSendService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;
    private int authNumber;



    public boolean CheckAuthNum(String email,String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else return redisUtil.getData(authNum).equals(email);
    }

    //임의의 6자리 양수를 반환
    public void makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < 6; i++) {
            randomNumber.append(Integer.toString(r.nextInt(10)));
        }

        authNumber = Integer.parseInt(randomNumber.toString());
    }


    public void joinEmail(String toMail) {
        makeRandomNumber();
        String setFrom = "tripnote10@gmail.com";
        String title = "Tripnote 인증코드가 도착했습니다."; // 이메일 제목
        String content =
                "Tripnote를 방문해주셔서 감사합니다." + 	//html 형식으로 작성
                        "<br><br>" +
                        "인증 번호는 <b>" + authNumber + "</b> 입니다." +
                        "<br>";
        mailSend(setFrom, toMail, title, content);

        redisUtil.setDataExpire(String.valueOf(authNumber), toMail, 180); // 유효시간 3분으로 설정
        log.info("인증 코드 : " + authNumber);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {// true를 전달하여 multipart 형식의 메시지를 지원, "utf-8"을 전달하여 문자 인코딩을 설정
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");

            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html로 설정
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }

    }

    // 비밀번호 재설정 로직
    @Transactional
    public void resetPassword(String email, String authNum) {
        // 인증 번호 확인
        if (!CheckAuthNum(email, authNum)) {
            throw new CustomException(ErrorCode.INVALID_AUTH_CODE);
        }

        // 이메일 존재 여부 확인
        if (!memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.NO_MEMBER);
        }

        // 임시 비밀번호 생성
        String tempPassword = generateTempPassword();
        String newPassword = bCryptPasswordEncoder.encode(tempPassword);

        // DB에 임시 비밀번호 업데이트
        memberRepository.updatePassword(email, newPassword);

        // 임시 비밀번호 이메일 전송
        String setFrom = "tripnote10@gmail.com";
        String title = "Tripnote 임시 비밀번호가 발급되었습니다.";
        String content =
                "Tripnote를 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "임시 비밀번호는 <b>" + tempPassword + "</b> 입니다." +
                        "<br>" +
                        "로그인 후 비밀번호를 변경해 주세요.";
        mailSend(setFrom, email, title, content);
    }

    // 임시 비밀번호 생성 (UUID 사용)
    private String generateTempPassword() {
        String tempPassword = UUID.randomUUID().toString().replace("-", "");
        return tempPassword.substring(0, 10);
    }
}