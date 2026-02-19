package com.example.spring.auth;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.libs.Email;
import com.example.spring.libs.Sms;
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

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구 (BCrypt 등)

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

    /**
     * 아이디 찾기 요청 처리 (POST 방식)
     * - 사용자가 입력한 정보(UserDto: 이름, 전화번호/이메일)를 기준으로 사용자 정보 조회
     * - 조회된 경우 해당 아이디를 FlashAttribute로 전달
     * - 조회되지 않은 경우 에러 메시지를 전달
     *
     * @param user 사용자 입력 정보 (username, phone 또는 email 포함)
     * @param request 현재 요청 객체
     * @param redirectAttributes 리다이렉트 시 메시지를 담기 위한 객체
     * @return 아이디 찾기 결과를 다시 같은 화면으로 리다이렉트
     */
    @PostMapping("/find-user-id")
    public String findUserIdPost(UserDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 입력된 정보를 기반으로 사용자 조회
        UserDto existsUser = userService.read(user);

        if (existsUser != null) {
            // 사용자 존재: 아이디를 문자로 전달
            if (user.getPhone() != null) {
                Sms coolSMS = new Sms();
                coolSMS.sendCoolsms("사용자 아이디는 " + existsUser.getUserId() + " 입니다.", user.getPhone());
            }
            // 사용자 존재: 아이디를 이메일로 전달
            else if (user.getEmail() != null) {
                Email emailService = new Email();
                emailService.sendNaverEmail("아이디 찾기", "사용자 아이디는 " + existsUser.getUserId() + " 입니다.", user.getEmail());
            }

            // 사용자 존재: 아이디를 성공 메시지로 전달
            redirectAttributes.addFlashAttribute("successMessage", "사용자 아이디는 " + existsUser.getUserId() + " 입니다.");
        } else {
            // 사용자 없음: 실패 메시지 전달
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        }

        // 결과 메시지를 전달하고 다시 아이디 찾기 화면으로 리다이렉트
        return "redirect:/auth/find-user-id";
    }

    /**
     * 비밀번호 초기화 화면 요청 처리 (GET 방식)
     *
     * @param request 현재 요청 객체 (세션 확인용)
     * @return 비밀번호 초기화 폼(auth/resetPassword.jsp) 또는 리다이렉트 경로
     */
    @GetMapping("/reset-password")
    public String resetPasswordGet(HttpServletRequest request) {
        // 비밀번호 초기화 화면 제공
        return "auth/resetPassword";
    }

    /**
     * 비밀번호 초기화 요청 처리 (POST 방식)
     * - 사용자 정보를 기반으로 사용자 존재 여부 확인
     * - 존재하는 경우 임시 비밀번호(6자리 숫자)로 비밀번호를 재설정하고 암호화 후 저장
     * - 초기화 결과에 따라 성공/실패 메시지를 FlashAttribute로 전달
     *
     * @param user 사용자 조회를 위한 정보 (이름 + 전화번호 or 이메일)
     * @param request HTTP 요청 객체
     * @param redirectAttributes 리다이렉트 시 메시지를 전달할 객체
     * @return 로그인 페이지 또는 초기화 폼으로 리다이렉트
     */
    @PostMapping("/reset-password")
    public String resetPasswordPost(UserDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 사용자 정보 조회
        UserDto existsUser = userService.read(user);

        if (existsUser != null) {
            // 6자리 임시 비밀번호 생성
            int iValue = (int)(Math.random() * 1000000);
            String newPassword = String.format("%06d", iValue); // 앞자리 0 포함되도록

            // 비밀번호 초기화 및 업데이트
            existsUser.setPassword(newPassword);
            boolean result = userService.update(existsUser);

            if (result) {
                // 사용자 존재: 비밀번호를 문자로 전달
                if (user.getPhone() != null) {
                    Sms coolSMS = new Sms();
                    coolSMS.sendCoolsms("초기화된 비밀번호는 " + newPassword + " 입니다.", user.getPhone());
                }
                // 사용자 존재: 비밀번호를 이메일로 전달
                else if (user.getEmail() != null) {
                    Email emailService = new Email();
                    emailService.sendNaverEmail("비밀번호 찾기", "초기화된 비밀번호는 " + newPassword + " 입니다.", user.getEmail());
                }

                redirectAttributes.addFlashAttribute("successMessage", "임시 비밀번호는 " + newPassword + " 입니다.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 초기화에 실패했습니다.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "사용자를 찾을 수 없습니다.");
        }

        return "redirect:/auth/reset-password";
    }

    /**
     * 로그인 화면 요청 처리 (GET 방식)
     *
     * @param request 현재 요청 객체 (세션 확인용)
     * @return 로그인 화면 또는 게시글 목록 리다이렉트
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        // 사용자에게 로그인 폼 제공
        return "auth/login";
    }

    /**
     * 로그인 요청 처리 (POST 방식)
     * - 사용자 ID와 비밀번호를 검증하고 로그인 성공 시 세션에 정보 저장
     *
     * @param user 로그인 폼에서 입력된 사용자 정보 (userId, password)
     * @param request HTTP 요청 객체 (세션 접근용)
     * @param redirectAttributes 리다이렉트 시 메시지를 담기 위한 객체
     * @return 로그인 성공 시 마이페이지로 이동, 실패 시 로그인 페이지로 리다이렉트
     */
    @PostMapping("/login")
    public String loginPost(UserDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 사용자 정보 조회 (userId 기준)
        UserDto existsUser = userService.read(user);

        // 사용자 존재 여부 및 비밀번호 검증
        if (existsUser != null && passwordEncoder.matches(user.getPassword(), existsUser.getPassword())) {
            // 세션에 로그인 정보 저장
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", existsUser.getUserId());
            session.setAttribute("username", existsUser.getUsername());
            session.setAttribute("role", existsUser.getRole());

            return "redirect:/profile"; // /profile 로 이동
        }

        // 로그인 실패: 에러 메시지와 함께 로그인 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("errorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
        return "redirect:/auth/login";
    }

    /**
     * 로그아웃 요청 처리 (GET 방식)
     * - 현재 세션이 존재하면 무효화하고 로그인 페이지로 리다이렉트
     *
     * @param request HTTP 요청 객체 (세션 접근용)
     * @return 로그인 페이지로 리다이렉트
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 현재 세션이 존재하면 삭제 (false 옵션: 세션이 없으면 null 반환)
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 무효화
        }

        return "redirect:/auth/login";
    }

    /**
     * 사용자 아이디 중복 체크 (POST 방식, AJAX 처리)
     * - 클라이언트에서 입력한 아이디가 이미 존재하는지 확인
     * - JSON 형태로 결과 반환: { "exists": true } 또는 { "exists": false }
     *
     * @param user 아이디(UserDto.userId)를 포함한 사용자 객체
     * @return JSON 응답 (exists: true/false)
     */
    @PostMapping("/check-user-id")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkUserIdPost(UserDto user) {
        // 입력된 아이디 기준으로 사용자 조회
        UserDto existsUser = userService.read(user);

        // 응답용 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("exists", existsUser != null);

        // JSON 형식으로 결과 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 사용자 전화번호 중복 체크 (POST 방식, AJAX 처리)
     * - 클라이언트에서 입력한 전화번호가 이미 등록된 사용자에게 있는지 확인
     * - JSON 형식으로 결과 반환: { "exists": true } 또는 { "exists": false }
     *
     * @param user 전화번호를 포함한 사용자 정보(UserDto.phone)
     * @return 중복 여부를 담은 JSON 응답
     */
    @PostMapping("/check-phone")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkPhonePost(UserDto user) {
        // 입력된 전화번호 기준으로 사용자 조회
        UserDto existsUser = userService.read(user);

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("exists", existsUser != null);

        // JSON 형식으로 응답 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * 사용자 이메일 중복 체크 (POST 방식, AJAX 처리)
     * - 사용자가 입력한 이메일이 이미 존재하는지 확인
     * - JSON 형식으로 결과 반환: { "exists": true } 또는 { "exists": false }
     *
     * @param user 이메일을 담은 UserDto 객체
     * @return 이메일 중복 여부를 포함한 JSON 응답
     */
    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkEmailPost(UserDto user) {
        // 입력된 이메일 기준으로 사용자 정보 조회
        UserDto existsUser = userService.read(user);

        // 응답 데이터 생성
        Map<String, Object> response = new HashMap<>();
        response.put("exists", existsUser != null);

        // JSON 응답 반환
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
