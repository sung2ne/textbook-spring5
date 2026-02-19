package com.example.spring.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * - 로그인, 로그아웃, 회원가입 등
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    /**
     * 회원가입 화면 요청 처리 (GET 방식)
     *
     * @param request 현재 요청 객체 (세션 확인용)
     * @return 회원가입 뷰
     */
    @GetMapping("/register")
    public String register(HttpServletRequest request) {
        // 회원가입 화면으로 이동
        return "auth/register";
    }
}
