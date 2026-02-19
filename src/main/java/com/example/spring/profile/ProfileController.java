package com.example.spring.profile;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
