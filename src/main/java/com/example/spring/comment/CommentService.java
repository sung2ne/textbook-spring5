package com.example.spring.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 컨트롤러와 DAO 사이에서 중간 역할을 수행
 */
@Service // Spring이 이 클래스를 서비스 컴포넌트로 인식하여 Bean으로 등록
public class CommentService {

    @Autowired
    CommentDao commentDao;

    /**
     * 댓글을 등록하는 메서드
     * @param comment 사용자가 작성한 댓글 정보
     * @return 등록한 댓글 ID
     */
    public int create(CommentDto comment) {
        // DAO를 호출하여 댓글을 DB에 저장하고 결과를 반환
        int result = commentDao.create(comment);
        return result;
    }

    /**
     * 댓글 목록을 조회하고 반환하는 메서드
     *
     * @param authPostId 게시글 번호
     * @return 게시글에 해당하는 댓글 목록
     */
    public List<CommentDto> list(int authPostId) {
        // 댓글 목록 조회
        List<CommentDto> comments = commentDao.list(authPostId);
        return comments;
    }
}
