package com.example.spring.libs;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 네이버 SMTP를 통한 이메일 발송 유틸리티 클래스
 */
public class Email {

    // 이메일 발송 메서드
    public void sendNaverEmail(String mailSubject, String mailContent, String mailTo) {
        // 보내는 사람 이메일 주소 (SMTP 인증 계정과 동일하게 설정 필요)
        String mailFrom = "보내는사람@naver.com";

        // SMTP 서버 설정
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");          // 네이버 SMTP 서버
        props.put("mail.smtp.port", "587");                     // 포트번호
        props.put("mail.smtp.auth", "true");                    // 인증 필요
        props.put("mail.smtp.starttls.enable", "true");         // TLS 보안 사용
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");        // 프로토콜 지정 (권장)

        // SMTP 인증 정보 설정
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("네이버아이디", "비밀번호");
            }
        });

        try {
            // 메일 작성
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailFrom));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            msg.setSubject(mailSubject);
            msg.setContent(mailContent, "text/html; charset=utf-8"); // HTML 본문

            // 메일 전송
            Transport.send(msg);

        } catch (MessagingException e) {
            System.out.println("이메일 전송 실패: " + e.getMessage());
        }
    }
}
