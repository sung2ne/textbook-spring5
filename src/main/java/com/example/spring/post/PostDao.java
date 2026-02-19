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

@Component
public class PostDao {
    
    private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
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
            posts = sqlSession.selectList("postMapper.list");
        } catch (DataAccessException e) {
            logger.error("게시글 목록 오류 : {}", e.getMessage(), e);
        }

        return posts;
    }

    /**
     * 게시글을 데이터베이스에 저장하는 메서드 (MyBatis 기반)
     * @param post 사용자가 작성한 게시글 데이터
     * @return 삽입된 게시글 id (성공 시 post.getId()에 자동 주입됨, 실패 시 -1)
     */
    public int create(PostDto post) {
        int result = -1;

        try {
            // MyBatis 매퍼의 postMapper.create 구문 실행
            // useGeneratedKeys="true"와 keyProperty="id"가 설정되어 있어 post.id에 자동으로 삽입된 ID가 주입됨
            result = sqlSession.insert("postMapper.create", post);

            // insert()는 삽입된 행 수를 반환하므로, 성공 시 post.getId()에서 생성된 게시글 ID를 확인 가능
            return result > 0 ? post.getId() : -1;

        } catch (DataAccessException e) {
            logger.error("게시글 등록 오류 : {}", e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 게시글 ID를 기준으로 게시글을 조회하는 메서드
     * @param id 조회할 게시글의 id
     * @return 게시글 정보(PostDto), 조회 실패 시 null 반환
     */
    public PostDto read(int id) {
        String query = "SELECT id, title, content, username, password, created_at, updated_at FROM posts WHERE id = ? LIMIT 1";

        PostDto post = null;

        try {
            post = jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(PostDto.class), id);
        } catch (DataAccessException e) {
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
        String query = "UPDATE posts SET title = ?, content = ?, username = ?, password = ? WHERE id = ? LIMIT 1";
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
        String query = "DELETE FROM posts WHERE id = ? LIMIT 1";
        int result = -1;

        try {
            result = jdbcTemplate.update(query, id);
        } catch (DataAccessException e) {
            logger.error("게시글 삭제 오류: {}", e.getMessage(), e);
        }

        return result;
    }
}
