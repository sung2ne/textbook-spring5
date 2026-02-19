package com.example.spring.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 게시글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * Controller와 DAO 사이에서 중간 역할을 수행
 */
@Service  // Spring이 이 클래스를 서비스 컴포넌트로 인식하여 Bean으로 등록
public class PostService {

    @Autowired  // PostDao 객체를 자동으로 주입받음
    PostDao postDao;

    /**
     * 게시글 목록을 조회하는 메서드
     * @return 게시글 리스트 (List<PostDto>)
     */
    public List<PostDto> list() {
        return postDao.list();  // DAO를 통해 DB에서 게시글 목록을 가져옴
    }
}
