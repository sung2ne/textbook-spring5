package com.example.spring.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Component // 해당 클래스가 Spring의 Bean으로 등록되도록 지정
public class PostDao {

    // 로그 출력을 위한 Logger 객체 생성
    private static final Logger logger = LoggerFactory.getLogger(PostDao.class);

    @Autowired // Spring이 SqlSessionTemplate 객체를 자동으로 주입
    private SqlSessionTemplate sqlSession;

    /**
     * 게시글 목록을 조회하는 메서드 (페이징 및 검색 기능 포함)
     * - 검색 조건이 주어지면 해당 조건(title, content, username 등)에 따라 필터링된 결과를 조회
     * - 검색 조건이 없으면 전체 게시글을 조회
     * - 페이징 처리를 위해 offset과 limit(페이지당 게시글 수)도 함께 전달
     *
     * @param offset 조회 시작 위치 (예: 0부터 시작, LIMIT offset, count 에서 사용)
     * @param listCountPerPage 한 페이지에 표시할 게시글 수
     * @param searchType 검색 기준 ("title", "content", "username", "all" 중 하나)
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 조회)
     * @return 게시글 리스트 (List<PostDto>), 실패 시 null 또는 빈 리스트 반환
     */
    public List<PostDto> list(int offset, int listCountPerPage, String searchType, String searchKeyword) {
        // 쿼리에 전달할 파라미터 구성
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("listCountPerPage", listCountPerPage);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);

        List<PostDto> posts = null;

        try {
            // MyBatis 매퍼(postMapper.xml)의 list 쿼리 실행
            posts = sqlSession.selectList("postMapper.list", params);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("게시글 목록 오류 : {}", e.getMessage(), e);
        }

        return posts;
    }

    /**
     * 게시글을 데이터베이스에 저장하는 메서드 (MyBatis 기반)
     * @param post 사용자가 작성한 게시글 데이터
     * @return 삽입된 게시글 ID (성공 시 post.getId()에 자동 주입됨, 실패 시 -1)
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
     * 게시글 ID를 기준으로 단건 조회하는 메서드
     * MyBatis 매퍼(postMapper.read)를 호출하여 게시글 1건을 조회함
     *
     * @param id 조회할 게시글의 ID
     * @return PostDto 객체 (조회된 게시글), 실패 시 null 반환
     */
    public PostDto read(int id) {
        PostDto post = null;

        try {
            // postMapper.xml에 정의된 <select id="read"> 구문 실행
            post = sqlSession.selectOne("postMapper.read", id);
        } catch (DataAccessException e) {
            // SQL 실행 중 예외 발생 시 로그 출력
            logger.error("게시글 보기 오류 : {}", e.getMessage(), e);
        }

        return post;
    }

    /**
     * 게시글을 수정하는 메서드
     * MyBatis 매퍼(postMapper.update)를 호출하여 게시글 정보를 DB에 반영함
     *
     * @param post 수정할 게시글 정보 (ID 포함)
     * @return 수정된 행 수 (성공 시 1, 실패 또는 오류 시 -1)
     */
    public int update(PostDto post) {
        int result = -1;

        try {
            // postMapper.xml의 <update id="update"> 구문 실행
            result = sqlSession.update("postMapper.update", post);
        } catch (DataAccessException e) {
            // SQL 실행 중 오류 발생 시 로그 출력
            logger.error("게시글 수정 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 게시글을 삭제하는 메서드
     * MyBatis 매퍼(postMapper.delete)를 호출하여 ID에 해당하는 게시글을 삭제함
     *
     * @param id 삭제할 게시글의 ID
     * @return 삭제된 행 수 (성공 시 1, 실패 또는 예외 시 -1)
     */
    public int delete(int id) {
        int result = -1;

        try {
            // postMapper.xml의 <delete id="delete"> 구문 실행
            result = sqlSession.delete("postMapper.delete", id);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("게시글 삭제 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 전체 게시글 수를 조회하는 메서드 (검색 조건 포함)
     * - 검색 조건이 주어진 경우 해당 조건(title, content, username 등)에 맞는 게시글 수를 반환
     * - 검색 조건이 없을 경우 전체 게시글 수를 반환
     * - 페이징 처리를 위한 totalCount 계산에 사용됨
     *
     * @param searchType 검색 기준 ("title", "content", "username", "all" 중 하나)
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 게시글 수 조회)
     * @return 조건에 해당하는 게시글 수 (int)
     */
    public int totalCount(String searchType, String searchKeyword) {
        // 검색 조건을 파라미터 맵에 담아 전달
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);

        // MyBatis 매퍼(postMapper.totalCount) 실행 후 게시글 수 반환
        return sqlSession.selectOne("postMapper.totalCount", params);
    }
}
