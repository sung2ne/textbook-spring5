package com.example.spring.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
