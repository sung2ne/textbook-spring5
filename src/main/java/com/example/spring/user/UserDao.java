package com.example.spring.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * 사용자 관련 데이터베이스 작업을 처리하는 DAO 클래스
 * - 회원 가입, 사용자 정보 조회 등을 수행
 */
@Repository
public class UserDao {

    @Autowired
    private SqlSession sqlSession; // MyBatis SQL 세션

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    /**
     * 사용자 등록
     * - MyBatis 매퍼(userMapper.create)를 호출하여 USERS 테이블에 사용자 정보를 삽입
     * 
     * @param user 가입할 사용자 정보(UserDto)
     * @return 삽입 성공 시 1, 실패 시 -1
     */
    public int create(UserDto user) {
        int result = -1;

        try {
            result = sqlSession.insert("userMapper.create", user);
        } catch (DataAccessException e) {
            logger.error("사용자 등록 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 사용자 단건 조회
     * - 주어진 조건(userId, username, phone, email 중 일부 또는 전부)에 해당하는 사용자 1명 조회
     * - userMapper.xml의 <select id="read"> 쿼리를 실행함
     *
     * @param user 조회 조건이 담긴 UserDto 객체
     * @return 조회된 사용자 정보(UserDto), 없을 경우 null 반환
     */
    public UserDto read(UserDto user) {
        UserDto result = null;

        try {
            // MyBatis 매퍼 호출 (조건은 userDto 객체에 포함된 필드 기준으로 동적 적용됨)
            result = sqlSession.selectOne("userMapper.read", user);
        } catch (Exception e) {
            logger.error("사용자 조회 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 사용자 정보 수정
     * - 전달받은 UserDto의 필드 중 null이 아닌 값만 DB에 반영
     * - userMapper.xml의 <update id="update"> 쿼리를 호출
     *
     * @param user 수정할 사용자 정보가 담긴 UserDto 객체
     * @return 수정된 행 수 (성공 시 1, 실패 시 0 또는 -1)
     */
    public int update(UserDto user) {
        int result = -1;

        try {
            result = sqlSession.update("userMapper.update", user);
        } catch (DataAccessException e) {
            logger.error("사용자 수정 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 사용자 삭제 메서드
     * - 주어진 사용자 ID를 기반으로 DB에서 사용자 정보를 삭제함
     * - MyBatis 매퍼의 userMapper.delete 쿼리를 호출
     *
     * @param userId 삭제할 사용자 ID
     * @return 삭제된 행 수 (성공 시 1, 실패 시 -1)
     */
    public int delete(String userId) {
        int result = -1;

        try {
            result = sqlSession.delete("userMapper.delete", userId);
        } catch (DataAccessException e) {
            logger.error("사용자 삭제 오류 : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * 사용자 목록을 조회하는 메서드 (페이징 및 검색 기능 포함)
     * - 검색 조건이 주어지면 해당 조건(title, content, username 등)에 따라 필터링된 결과를 조회
     * - 검색 조건이 없으면 전체 사용자를 조회
     * - 페이징 처리를 위해 offset과 limit(페이지당 사용자 수)도 함께 전달
     *
     * @param offset 조회 시작 위치 (예: 0부터 시작, LIMIT offset, count 에서 사용)
     * @param listCountPerPage 한 페이지에 표시할 사용자 수
     * @param searchType 검색 기준 ("title", "content", "username", "all" 중 하나)
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 조회)
     * @return 사용자 리스트 (List<PostDto>), 실패 시 null 또는 빈 리스트 반환
     */
    public List<UserDto> list(int offset, int listCountPerPage, String searchType, String searchKeyword) {
        // 쿼리에 전달할 파라미터 구성
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("listCountPerPage", listCountPerPage);
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);

        List<UserDto> users = null;

        try {
            // MyBatis 매퍼(userMapper.xml)의 list 쿼리 실행
            users = sqlSession.selectList("userMapper.list", params);
        } catch (DataAccessException e) {
            // 예외 발생 시 로그 출력
            logger.error("사용자 목록 오류 : {}", e.getMessage(), e);
        }

        return users;
    }

    /**
     * 전체 사용자 수를 조회하는 메서드 (검색 조건 포함)
     * - 검색 조건이 주어진 경우 해당 조건(title, content, username 등)에 맞는 사용자 수를 반환
     * - 검색 조건이 없을 경우 전체 사용자 수를 반환
     * - 페이징 처리를 위한 totalCount 계산에 사용됨
     *
     * @param searchType 검색 기준 ("title", "content", "username", "all" 중 하나)
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 사용자 수 조회)
     * @return 조건에 해당하는 사용자 수 (int)
     */
    public int totalCount(String searchType, String searchKeyword) {
        // 검색 조건을 파라미터 맵에 담아 전달
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("searchKeyword", searchKeyword);

        // MyBatis 매퍼(userMapper.totalCount) 실행 후 사용자 수 반환
        return sqlSession.selectOne("userMapper.totalCount", params);
    }
}
