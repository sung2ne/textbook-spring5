package com.example.spring.post;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component  // 해당 클래스가 Spring의 Bean으로 등록되도록 지정
public class PostDao {

    // 로그 출력을 위한 Logger 객체 생성
    private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

    @Autowired  // Spring이 JdbcTemplate 객체를 자동으로 주입
    JdbcTemplate jdbcTemplate;

    /**
     * 게시글 목록을 데이터베이스에서 조회하는 메서드
     * @return 게시글(PostDto) 리스트
     */
    public List<PostDto> list() {
        // 게시글을 id 내림차순으로 정렬하여 전체 조회하는 SQL 쿼리
        String query = "select id, title, content, username, password, created_at, updated_at from posts order by id desc";

        List<PostDto> posts = null;

        try {
            // 쿼리 실행 후 결과를 PostDto 객체 목록으로 매핑
            posts = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(PostDto.class));
        } catch (DataAccessException e) {
            // 데이터베이스 조회 중 오류 발생 시 로그 출력
            logger.error("게시글 목록 오류 : {}", e.getMessage(), e);
        }

        return posts;
    }
}
