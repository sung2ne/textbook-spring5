package com.example.spring.authPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.libs.Pagination;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 컨트롤러와 DAO 사이에서 중간 역할을 수행
 */
@Service // Spring이 이 클래스를 서비스 컴포넌트로 인식하여 Bean으로 등록
public class AuthPostService {

    @Autowired
    AuthPostDao authPostDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 게시글 목록을 조회하고 검색 조건 및 페이징 정보를 함께 반환하는 메서드
     *
     * - 검색 조건이 주어지면 해당 조건(title, content, userId, username, phone, email 등)에 따라 게시글을 필터링
     * - 전체 게시글 수(totalCount)를 기반으로 Pagination 객체를 생성
     * - 계산된 offset을 이용하여 해당 페이지의 게시글만 조회
     * - 검색 조건과 게시글 목록을 Map 형태로 반환하여 뷰에서 쉽게 사용 가능
     *
     * @param currentPage 현재 페이지 번호
     * @param listCountPerPage 한 페이지에 보여줄 게시글 수
     * @param pageCountPerPage 한 번에 보여줄 페이지 번호 수 (예: 하단에 5개씩)
     * @param searchType 검색 기준 ("title", "content", "username", "all")
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 조회)
     * @return Map<String, Object> 형태의 결과
     *         - posts: 게시글 리스트 (List<AuthPostDto>)
     *         - searchType: 검색 기준 (뷰에서 유지)
     *         - searchKeyword: 검색어 (뷰에서 유지)
     *         - pagination: 페이지네이션 정보 (페이지 버튼 출력용)
     */
    public Map<String, Object> list(int currentPage, int listCountPerPage, int pageCountPerPage, String searchType, String searchKeyword) {
        // 검색 조건에 따른 전체 게시글 수 조회
        int totalCount = authPostDao.totalCount(searchType, searchKeyword);

        // 페이지네이션 객체 생성 (총 게시글 수 기반으로 계산)
        Pagination pagination = new Pagination(currentPage, listCountPerPage, pageCountPerPage, totalCount);

        // 페이징 정보에 따른 게시글 목록 조회 (LIMIT offset, count)
        List<AuthPostDto> posts = authPostDao.list(pagination.offset(), listCountPerPage, searchType, searchKeyword);

        // 결과 데이터 맵 구성
        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);
        result.put("pagination", pagination); // 뷰에서 페이지 번호 출력에 사용

        return result;
    }

    /**
     * 게시글을 등록하는 메서드
     * @param post 사용자가 작성한 게시글 정보
     * @return 등록한 게시글 ID
     */
    public int create(AuthPostDto post) {
        // DAO를 호출하여 게시글을 DB에 저장하고 결과를 반환
        int result = authPostDao.create(post);
        return result;
    }

    /**
     * 특정 게시글을 조회하는 메서드
     * @param id 조회할 게시글의 ID
     * @return 게시글(AuthPostDto) 객체, 없으면 null
     */
    public AuthPostDto read(int id) {
        // DAO를 통해 ID에 해당하는 게시글을 조회
        return authPostDao.read(id);
    }

    /**
     * 게시글을 수정하는 메서드
     * @param post 수정할 게시글 정보
     * @return 수정 성공 여부 (true: 성공, false: 실패)
     */
    public boolean update(AuthPostDto post) {
        int result = authPostDao.update(post);
        return result > 0;
    }

    /**
     * 게시글을 삭제하는 메서드
     * - 비밀번호 검증 후 삭제 처리
     * @param post 삭제할 게시글 정보
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     */
    public boolean delete(AuthPostDto post) {
        int result = authPostDao.delete(post.getId());
        return result > 0;
    }
}
