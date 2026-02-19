package com.example.spring.libs;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoEmptyResponseException;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.exception.NurigoUnknownException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

/**
 * CoolSMS를 사용한 문자 발송 유틸리티 클래스
 */
public class Sms {

    // 실제 발신 번호 및 인증 정보는 외부 설정에서 주입하거나 환경변수로 관리하는 것이 바람직함
    private static final String API_KEY = "본인의 API KEY";         // TODO: 환경변수로 관리 권장
    private static final String API_SECRET = "본인의 SECRET KEY";   // TODO: 환경변수로 관리 권장
    private static final String API_URL = "https://api.coolsms.co.kr";
    private static final String FROM_NUMBER = "본인의 전화번호";      // 발신자 번호

    private final DefaultMessageService messageService;

    // 생성자에서 SDK 초기화
    public Sms() {
        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET, API_URL);
    }

    /**
     * 문자 메시지를 전송하는 메서드
     *
     * @param smsMessage 전송할 메시지 본문
     * @param phone 수신자 전화번호
     */
    public void sendCoolsms(String smsMessage, String phone) {
        // 메시지 객체 생성 및 정보 설정
        Message message = new Message();
        message.setFrom(FROM_NUMBER);
        message.setTo(phone);
        message.setText(smsMessage);

        try {
            // 메시지 전송
            messageService.send(message);
        } catch (NurigoMessageNotReceivedException e) {
            // 전송 실패한 메시지 목록 출력
            System.out.println("전송 실패 메시지 목록: " + e.getFailedMessageList());
            System.out.println("에러 메시지: " + e.getMessage());
        } catch (NurigoEmptyResponseException | NurigoUnknownException e) {
            System.out.println("기타 예외 발생: " + e.getMessage());
        }
    }
}
