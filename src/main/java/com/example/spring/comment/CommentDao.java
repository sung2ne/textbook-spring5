package com.example.spring.comment;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component // 해당 클래스가 Spring의 Bean으로 등록되도록 지정
public class CommentDao {

    // 로그 출력을 위한 Logger 객체 생성
    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    @Autowired // Spring이 SqlSessionTemplate 객체를 자동으로 주입
    private SqlSessionTemplate sqlSession;

    /**
     * 댓글을 데이터베이스에 저장하는 메서드 (MyBatis 기반)
     * @param comment 사용자가 작성한 댓글 데이터
     * @return 삽입된 댓글 ID (성공 시 comment.getId()에 자동 주입됨, 실패 시 -1)
     */
    public int create(CommentDto comment) {
        int result = -1;

        try {
            // MyBatis 매퍼의 commentMapper.create 구문 실행
            // useGeneratedKeys="true"와 keyProperty="id"가 설정되어 있어 comment.id에 자동으로 삽입된 ID가 주입됨
            result = sqlSession.insert("commentMapper.create", comment);

            // insert()는 삽입된 행 수를 반환하므로, 성공 시 comment.getId()에서 생성된 댓글 ID를 확인 가능
            return result > 0 ? comment.getId() : -1;

        } catch (DataAccessException e) {
            logger.error("댓글 등록 오류 : {}", e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 댓글 목록을 조회하는 메서드
     *
     * @param authPostId 게시글 번호
     * @return 댓글 리스트 (List<CommentDto>), 실패 시 null 또는 빈 리스트 반환
     */
    public List<CommentDto> list(int authPostId) {
        List<CommentDto> comments = null;

        try {
            // MyBatis 매퍼(commentMapper.xml)의 list 쿼리 실행
            comments = sqlSession.selectList("commentMapper.list", authPostId);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("댓글 목록 오류 : {}", e.getMessage(), e);
        }

        return comments;
    }
}
