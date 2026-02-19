package com.example.spring.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 컨트롤러와 DAO 사이에서 중간 역할을 수행
 */
@Service // Spring이 이 클래스를 서비스 컴포넌트로 인식하여 Bean으로 등록
public class PostService {

    @Autowired // PostDao 객체를 자동으로 주입받음
    PostDao postDao;

    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 비밀번호 검증 메서드
     * @param post 사용자가 입력한 게시글 정보(ID, 비밀번호 포함)
     * @return 비밀번호 일치 여부 (true: 일치, false: 불일치 또는 게시글 없음)
     */
    private boolean verifyPassword(PostDto post) {
        PostDto originalPost = postDao.read(post.getId());

        // 게시글 없음
        if (originalPost == null) return false;

        // matches(사용자입력비밀번호, DB해시비밀번호)
        return passwordEncoder.matches(post.getPassword(), originalPost.getPassword());
    }

    /**
     * 게시글 목록을 조회하고 검색 조건을 함께 반환하는 메서드
     * - 검색 조건이 있을 경우 필터링된 결과를 조회
     * - 검색 조건이 없을 경우 전체 게시글을 조회
     * - 검색 조건과 결과 목록을 Map 형태로 반환하여 뷰에서 사용 가능
     *
     * @param searchType 검색 기준 (예: "title", "content", "username")
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 목록 조회)
     * @return 게시글 목록 및 검색 조건을 담은 Map<String, Object>
     *         - posts: 게시글 리스트 (List<PostDto>)
     *         - searchType: 사용자가 입력한 검색 기준
     *         - searchKeyword: 사용자가 입력한 검색어
     */
    public Map<String, Object> list(String searchType, String searchKeyword) {
        // 게시글 목록 조회
        List<PostDto> posts = postDao.list(searchType, searchKeyword);

        // 결과 맵 생성 및 데이터 추가
        Map<String, Object> result = new HashMap<>();
        result.put("posts", posts);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);

        return result;
    }

    /**
     * 게시글을 등록하는 메서드
     * @param post 사용자가 작성한 게시글 정보
     * @return 등록한 게시글 ID
     */
    public int create(PostDto post) {
        // 비밀번호 암호화
        String password = passwordEncoder.encode(post.getPassword());
        post.setPassword(password);

        // DAO를 호출하여 게시글을 DB에 저장하고 결과를 반환
        int result = postDao.create(post);
        return result;
    }

    /**
     * 특정 게시글을 조회하는 메서드
     * @param id 조회할 게시글의 ID
     * @return 게시글(PostDto) 객체, 없으면 null
     */
    public PostDto read(int id) {
        // DAO를 통해 ID에 해당하는 게시글을 조회
        return postDao.read(id);
    }

    /**
     * 게시글을 수정하는 메서드
     * - 비밀번호 검증 후 수정 처리
     * @param post 수정할 게시글 정보 (ID, 비밀번호 포함)
     * @return 수정 성공 여부 (true: 성공, false: 실패)
     */
    public boolean update(PostDto post) {
        if (!verifyPassword(post)) {
            return false;
        }

        // 비밀번호 암호화
        String password = passwordEncoder.encode(post.getPassword());
        post.setPassword(password);

        int result = postDao.update(post);
        return result > 0;
    }

    /**
     * 게시글을 삭제하는 메서드
     * - 비밀번호 검증 후 삭제 처리
     * @param post 삭제할 게시글 정보 (ID, 비밀번호 포함)
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     */
    public boolean delete(PostDto post) {
        if (!verifyPassword(post)) {
            return false;
        }

        int result = postDao.delete(post.getId());
        return result > 0;
    }
}
