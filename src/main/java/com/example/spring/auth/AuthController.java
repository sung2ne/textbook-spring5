package com.example.spring.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.user.UserDto;
import com.example.spring.user.UserService;

/**
 * 인증 관련 요청을 처리하는 컨트롤러
 * - 로그인, 로그아웃, 회원가입 등
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

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

    /**
     * 회원 가입 요청 처리 (POST 방식)
     * - 사용자가 입력한 회원 정보를 UserService를 통해 등록
     * - 등록 성공 시 로그인 페이지로 이동
     * - 등록 실패 시 다시 회원가입 폼으로 이동하며 오류 메시지 표시
     *
     * @param user 사용자 입력 데이터(UserDto)
     * @param request 현재 HTTP 요청 (필요 시 세션 접근 가능)
     * @param redirectAttributes 리다이렉트 시 메시지를 전달할 객체 (Flash Attributes)
     * @return 리다이렉트 경로 (성공: /auth/login, 실패: /auth/register)
     */
    @PostMapping("/register")
    public String registerPost(UserDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 회원 가입 처리 (비밀번호 암호화 포함)
        boolean result = userService.create(user);

        if (result) {
            // 가입 성공 시 성공 메시지와 함께 로그인 페이지로 이동
            redirectAttributes.addFlashAttribute("successMessage", "회원 가입이 완료되었습니다.");
            return "redirect:/auth/login";
        }

        // 가입 실패 시 에러 메시지와 함께 회원가입 폼으로 리다이렉트
        redirectAttributes.addFlashAttribute("errorMessage", "회원 가입에 실패했습니다.");
        return "redirect:/auth/register";
    }

    /**
     * 아이디 찾기 화면 요청 처리 (GET 방식)
     *
     * @param request 현재 요청 객체 (세션 확인용)
     * @return 아이디 찾기 화면(auth/findUserId.jsp)
     */
    @GetMapping("/find-user-id")
    public String findUserIdGet(HttpServletRequest request) {
        // 아이디 찾기 화면으로 이동
        return "auth/findUserId";
    }
}
