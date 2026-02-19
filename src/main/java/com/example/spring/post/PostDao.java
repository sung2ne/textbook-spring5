package com.example.spring.post;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component // 해당 클래스가 Spring의 Bean으로 등록되도록 지정
public class PostDao {
    
    // 로그 출력을 위한 Logger 객체 생성
    private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

    @Autowired // Spring이 JdbcTemplate 객체를 자동으로 주입
    JdbcTemplate jdbcTemplate;

    @Autowired // Spring이 SqlSessionTemplate 객체를 자동으로 주입
    private SqlSessionTemplate sqlSession;

    /**
     * 게시글 목록을 데이터베이스에서 조회하는 메서드
     * MyBatis 매퍼(postMapper.xml)의 list 구문을 호출하여 전체 게시글을 조회함
     *
     * @return 게시글(PostDto) 리스트, 조회 실패 시 null 또는 빈 리스트 반환
     */
    public List<PostDto> list() {
        List<PostDto> posts = null;

        try {
            // MyBatis의 매퍼 네임스페이스(postMapper)와 id(list)를 지정하여 쿼리 실행
            posts = sqlSession.selectList("postMapper.list");
        } catch (DataAccessException e) {
            // 데이터 조회 중 오류가 발생한 경우 로그 출력
            logger.error("게시글 목록 오류 : {}", e.getMessage(), e);
        }

        return posts;
    }

    /**
     * 게시글을 데이터베이스에 저장하는 메서드
     * @param post 사용자가 작성한 게시글 데이터
     * @return 삽입된 게시글 id(성공 시 게시글 id, 실패 시 -1)
     */
    public int create(PostDto post) {
        // 게시글의 제목, 내용, 작성자, 비밀번호만 저장하며, 작성일시는 DB에서 자동 처리
        String query = "INSERT INTO posts (title, content, username, password) VALUES (?, ?, ?, ?)";

        // 마지막에 저장된 ID를 가져오는 쿼리
        String idQuery = "SELECT LAST_INSERT_ID()"; 

        int result = -1;

        try {
            // 게시글 저장 쿼리 실행
            jdbcTemplate.update(query,
                    post.getTitle(),
                    post.getContent(),
                    post.getUsername(),
                    post.getPassword());

            // 방금 삽입한 글의 id반환
            // LAST_INSERT_ID()는 동일한 DB 세션 내에서만 유효하므로 반드시 update() 직후에 호출해야 함
            result = jdbcTemplate.queryForObject(idQuery, Integer.class);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("게시글 등록 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 게시글 ID를 기준으로 게시글을 조회하는 메서드
     * @param id 조회할 게시글의 id
     * @return 게시글 정보(PostDto), 조회 실패 시 null 반환
     */
    public PostDto read(int id) {
        // 게시글 단건 조회 SQL 쿼리
        String query = "SELECT id, title, content, username, password, created_at, updated_at FROM posts WHERE id= ? LIMIT 1";

        PostDto post = null;

        try {
            // ID에 해당하는 게시글 조회 후 PostDto 객체로 반환
            post = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(PostDto.class), id);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("게시글 조회 오류 (id: {}): {}", id, e.getMessage(), e);
        }

        return post;
    }

    /**
     * 게시글을 수정하는 메서드
     * @param post 수정할 게시글 정보 (id 포함)
     * @return 수정된 행 수 (성공 시 1, 실패 시 -1)
     */
    public int update(PostDto post) {
        String query = "UPDATE posts SET title = ?, content = ?, username = ?, password = ? WHERE id= ? LIMIT 1";
        int result = -1;

        try {
            result = jdbcTemplate.update(query,
                    post.getTitle(),
                    post.getContent(),
                    post.getUsername(),
                    post.getPassword(),
                    post.getId());
        } catch (DataAccessException e) {
            logger.error("게시글 수정 오류: {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 게시글을 삭제하는 메서드
     * @param id 삭제할 게시글의 id
     * @return 삭제된 행 수 (성공 시 1, 실패 시 -1)
     */
    public int delete(int id) {
        String query = "DELETE FROM posts WHERE id= ? LIMIT 1";
        int result = -1;

        try {
            result = jdbcTemplate.update(query, id);
        } catch (DataAccessException e) {
            logger.error("게시글 삭제 오류: {}", e.getMessage(), e);
        }

        return result;
    }
}
