package com.example.spring.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 컨트롤러와 DAO 사이에서 중간 역할을 수행
 */
@Service // Spring이 이 클래스를 서비스 컴포넌트로 인식하여 Bean으로 등록
public class PostService {

    @Autowired // PostDao 객체를 자동으로 주입받음
    PostDao postDao;

    /**
     * 게시글 목록을 조회하는 메서드
     * @return 게시글 리스트 (List<PostDto>)
     */
    public List<PostDto> list() {
        return postDao.list(); // DAO를 통해 DB에서 게시글 목록을 가져옴
    }

    /**
     * 게시글을 등록하는 메서드
     * @param post 사용자가 작성한 게시글 정보
     * @return 등록 성공 여부 (true: 성공, false: 실패)
     */
    public boolean create(PostDto post) {
        // DAO를 호출하여 게시글을 DB에 저장하고 결과를 반환
        int result = postDao.create(post);
        return result > 0; // 1개 이상 행이 삽입되면 성공으로 판단
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
}
