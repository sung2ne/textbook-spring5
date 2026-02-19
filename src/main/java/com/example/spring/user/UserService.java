package com.example.spring.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 * - 회원 가입 시 비밀번호 암호화 처리 포함
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao; // DB 처리 담당

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화 도구 (BCrypt 등)

    /**
     * 사용자 등록 처리
     * - 사용자의 비밀번호를 암호화한 후 DB에 저장
     *
     * @param user 사용자가 입력한 회원가입 정보 (UserDto)
     * @return 등록 성공 여부 (true: 성공, false: 실패)
     */
    public boolean create(UserDto user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // 암호화된 비밀번호로 설정

        // 사용자 DB 등록
        int result = userDao.create(user);
        return result > 0;
    }
}
