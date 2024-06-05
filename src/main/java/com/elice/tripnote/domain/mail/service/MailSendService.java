package com.elice.tripnote.domain.mail.service;

import com.elice.tripnote.global.exception.CustomException;
import com.elice.tripnote.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailSendService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private  RedisUtil redisUtil;
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
        String title = "Tripnote 회원가입 인증코드가 도착했습니다."; // 이메일 제목
        String content =
                "Tripnote를 방문해주셔서 감사합니다." + 	//html 형식으로 작성
                        "<br><br>" +
                        "인증 번호는 <b>" + authNumber + "</b> 입니다." +
                        "<br>";
        mailSend(setFrom, toMail, title, content);

        redisUtil.setDataExpire(String.valueOf(authNumber), toMail, 180); // 유효시간 3분으로 설정
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

}