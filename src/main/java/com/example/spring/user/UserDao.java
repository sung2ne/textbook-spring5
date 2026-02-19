package com.example.spring.user;

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
}
