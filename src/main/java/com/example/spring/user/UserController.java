package com.example.spring.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 사용자(User) 관련 요청을 처리하는 컨트롤러 클래스
 * - 사용자 목록 조회 기능 제공
 */
@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * 사용자 목록 화면 요청 처리 (GET 방식)
     *
     * - 사용자가 검색 조건(searchType, searchKeyword)을 입력하면 해당 조건에 따라 사용자를 필터링
     * - 검색 조건이 없으면 전체 사용자를 조회
     * - 페이지 번호(page) 파라미터를 통해 해당 페이지의 사용자만 조회 (기본값은 1)
     * - 사용자 목록, 검색 조건, 페이지네이션 정보를 모델에 담아 뷰로 전달
     *
     * @param searchType 검색 기준 ("userId", "username", "phone", "email" 등), null 허용
     * @param searchKeyword 검색어, null 또는 빈 문자열 허용
     * @param currentPage 현재 페이지 번호 (기본값: 1)
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 사용자 목록을 출력할 뷰 이름 ("user/list.jsp")
     */
    @GetMapping("")
    public String listGet(
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchKeyword,
        @RequestParam(value = "page", defaultValue = "1") int currentPage,
        Model model
    ) {
        int listCountPerPage = 10;  // 한 페이지에서 불러올 사용자 수
        int pageCountPerPage = 5;   // 하단에 보여질 페이지 수 (예: [1][2][3][4][5])

        // 서비스 계층을 통해 사용자 목록 + 검색 조건 + 페이징 정보를 조회
        Map<String, Object> result = userService.list(
            currentPage, listCountPerPage, pageCountPerPage, searchType, searchKeyword
        );

        // 모델에 조회된 데이터 전달 (뷰에서 활용)
        model.addAttribute("users", result.get("users"));               // 사용자 목록
        model.addAttribute("pagination", result.get("pagination"));     // 페이지네이션 정보
        model.addAttribute("searchType", result.get("searchType"));     // 검색 기준
        model.addAttribute("searchKeyword", result.get("searchKeyword")); // 검색어

        // user/list.jsp 뷰 렌더링
        return "user/list";
    }

    /**
     * 사용자 상세보기 화면 요청 처리 (GET 방식)
     * - 사용자 ID를 통해 사용자 정보를 조회하여 화면에 전달
     *
     * @param userId 조회할 사용자 ID (URL 경로 변수)
     * @param model 조회한 사용자 정보를 뷰에 전달하기 위한 객체
     * @return 사용자 상세보기 화면 뷰 이름 ("user/read.jsp")
     */
    @GetMapping("/{userId}")
    public String readGet(@PathVariable("userId") String userId, Model model) {
        // 사용자 ID로 사용자 정보 조회
        UserDto user = new UserDto();
        user.setUserId(userId);
        user = userService.read(user);

        // 조회한 사용자 정보를 모델에 담아 뷰로 전달
        model.addAttribute("user", user);

        // 사용자 상세보기 화면 렌더링
        return "user/read";
    }

    /**
     * 사용자 삭제 요청 처리 (POST 방식)
     * - 비밀번호 검증 후 사용자 삭제
     *
     * @param userId 조회할 사용자 ID (URL 경로 변수)
     * @param redirectAttributes 삭제 결과 메시지 전달용 객체
     * @return 삭제 성공 시 목록 페이지로, 실패 시 상세보기 페이지로 리다이렉트
     */
    @PostMapping("/{userId}/delete")
    public String deletePost(String userId, RedirectAttributes redirectAttributes) {
        // 사용자 삭제 처리
        boolean result = userService.delete(userId);

        if (result) {
            // 삭제 성공: 성공 메시지 전달 후 사용자 목록으로 이동
            redirectAttributes.addFlashAttribute("successMessage", "사용자가 삭제되었습니다.");
            return "redirect:/users/";
        }

        // 삭제 실패: 에러 메시지 전달 후 사용자 상세보기 페이지로 이동
        redirectAttributes.addFlashAttribute("errorMessage", "사용자 삭제에 실패하였습니다.");
        return "redirect:/users/" + userId;
    }
}
