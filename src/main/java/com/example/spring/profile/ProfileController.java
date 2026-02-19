package com.example.spring.profile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.user.UserDto;
import com.example.spring.user.UserService;

/**
 * 사용자 프로필 관련 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    /**
     * 프로필 화면 요청 처리 (GET 방식)
     * - 로그인된 사용자의 userId를 세션에서 가져와 사용자 정보를 조회
     * - 사용자 정보를 모델에 담아 프로필 화면으로 전달
     *
     * @param model 뷰에 전달할 모델 객체
     * @param request HTTP 요청 객체 (세션 접근용)
     * @return 프로필 화면 뷰 이름 ("profile/profile.jsp")
     */
    @GetMapping("")
    public String profile(Model model, HttpServletRequest request) {
        // 세션에서 로그인된 사용자 ID 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // 사용자 정보 조회
        UserDto user = new UserDto();
        user.setUserId(userId);
        user = userService.read(user);

        // 모델에 사용자 정보 담기
        model.addAttribute("profile", user);

        // 프로필 화면 렌더링
        return "profile/profile";
    }

    /**
     * 프로필 수정 화면 요청 처리 (GET 방식)
     * - 로그인된 사용자의 userId를 세션에서 꺼내 사용자 정보를 조회
     * - 조회된 정보를 모델에 담아 수정 폼에 출력
     *
     * @param model 사용자 정보를 전달할 모델 객체
     * @param request 세션에서 로그인된 사용자 정보를 가져오기 위한 요청 객체
     * @return 프로필 수정 화면 뷰 이름 ("profile/updateProfile.jsp")
     */
    @GetMapping("/update-profile")
    public String updateProfileGet(Model model, HttpServletRequest request) {
        // 세션에서 로그인된 사용자 아이디 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // 사용자 정보 조회
        UserDto user = new UserDto();
        user.setUserId(userId);
        user = userService.read(user);

        // 모델에 사용자 정보 담기
        model.addAttribute("profile", user);

        // 프로필 수정 화면 렌더링
        return "profile/updateProfile";
    }

    /**
     * 프로필 수정 요청 처리 (POST 방식)
     * - 세션에서 로그인된 사용자 ID를 가져와 UserDto에 설정
     * - 입력된 사용자 정보를 기반으로 수정 처리 수행
     * - 결과에 따라 성공/실패 메시지를 플래시 속성으로 전달하고 리디렉트
     *
     * @param user 수정된 사용자 정보 (폼 입력값)
     * @param request 세션에서 로그인된 사용자 ID를 가져오기 위한 객체
     * @param redirectAttributes 리다이렉트 시 메시지를 전달하기 위한 객체
     * @return 수정 성공 시 프로필 페이지로, 실패 시 수정 폼으로 리디렉트
     */
    @PostMapping("/update-profile")
    public String updateProfilePost(UserDto user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 로그인된 사용자 ID 가져오기
        String userId = (String) request.getSession().getAttribute("userId");

        // UserDto에 세션 사용자 ID 설정
        user.setUserId(userId);

        // 사용자 정보 수정 처리
        boolean result = userService.update(user);

        // 수정 성공 시 메시지와 함께 프로필 페이지로 이동
        if (result) {
            redirectAttributes.addFlashAttribute("successMessage", "프로필이 수정되었습니다.");
            return "redirect:/profile";
        }

        // 수정 실패 시 에러 메시지 전달 후 다시 수정 페이지로 이동
        redirectAttributes.addFlashAttribute("errorMessage", "프로필 수정에 실패했습니다.");
        return "redirect:/profile/update-profile";
    }

    /**
     * 비밀번호 수정 화면 요청 처리 (GET 방식)
     * - 로그인된 사용자가 비밀번호를 변경할 수 있는 화면을 보여줌
     *
     * @return 비밀번호 수정 폼 뷰 이름 ("profile/updatePassword.jsp")
     */
    @GetMapping("/update-password")
    public String updatePasswordGet() {
        return "profile/updatePassword";
    }
}
