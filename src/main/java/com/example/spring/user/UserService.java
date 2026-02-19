package com.example.spring.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.spring.libs.Pagination;

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

    /**
     * 사용자 단건 조회
     * - userId, username, phone, email 등의 조건을 담은 UserDto를 기준으로 사용자 정보를 조회함
     * - UserDao.read()를 호출하여 DB에서 사용자 1명을 조회
     *
     * @param userVo 조회 조건이 담긴 UserDto 객체
     * @return 사용자 정보(UserDto), 없으면 null 반환
     */
    public UserDto read(UserDto userVo) {
        return userDao.read(userVo);
    }

    /**
     * 사용자 정보 수정
     * - 전달된 정보 중 비밀번호가 비어있지 않으면 암호화하여 반영
     * - 그 외 항목(username, phone, email)은 그대로 수정
     *
     * @param user 수정할 사용자 정보(UserDto)
     * @return 수정 성공 여부 (true: 성공, false: 실패)
     */
    public boolean update(UserDto user) {
        // 비밀번호가 비어 있지 않은 경우에만 암호화 처리
        if (!user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        // DAO를 통해 DB에 사용자 정보 수정 요청
        int result = userDao.update(user);

        // 수정된 행이 1건 이상이면 성공
        return result > 0;
    }

    /**
     * 사용자 삭제 서비스 메서드
     * - 전달받은 사용자 ID를 기준으로 해당 사용자를 DB에서 삭제
     * - Dao 계층의 delete 메서드를 호출하고 성공 여부를 boolean 값으로 반환
     *
     * @param userId 삭제할 사용자 ID
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     */
    public boolean delete(String userId) {
        int result = userDao.delete(userId);
        return result > 0;
    }

    /**
     * 사용자 목록을 조회하고 검색 조건 및 페이징 정보를 함께 반환하는 메서드
     * 
     * - 검색 조건이 주어지면 해당 조건(userId, username, phone, email 등)에 따라 사용자를 필터링
     * - 전체 사용자 수(totalCount)를 기반으로 Pagination 객체를 생성
     * - 계산된 offset을 이용하여 해당 페이지의 사용자만 조회
     * - 검색 조건과 사용자 목록을 Map 형태로 반환하여 뷰에서 쉽게 사용 가능
     *
     * @param currentPage 현재 페이지 번호
     * @param listCountPerPage 한 페이지에 보여줄 사용자 수
     * @param pageCountPerPage 한 번에 보여줄 페이지 번호 수 (예: 하단에 5개씩)
     * @param searchType 검색 기준 ("userId", "username", "phone", "email", "all")
     * @param searchKeyword 검색어 (null 또는 빈 문자열이면 전체 조회)
     * @return Map<String, Object> 형태의 결과
     *         - users: 사용자 리스트 (List<UserDto>)
     *         - searchType: 검색 기준 (뷰에서 유지)
     *         - searchKeyword: 검색어 (뷰에서 유지)
     *         - pagination: 페이지네이션 정보 (페이지 버튼 출력용)
     */
    public Map<String, Object> list(int currentPage, int listCountPerPage, int pageCountPerPage, String searchType, String searchKeyword) {
        // 검색 조건에 따른 전체 사용자 수 조회
        int totalCount = userDao.totalCount(searchType, searchKeyword);

        // 페이지네이션 객체 생성 (총 사용자 수 기반으로 계산)
        Pagination pagination = new Pagination(currentPage, listCountPerPage, pageCountPerPage, totalCount);

        // 페이징 정보에 따른 사용자 목록 조회 (LIMIT offset, count)
        List<UserDto> users = userDao.list(pagination.offset(), listCountPerPage, searchType, searchKeyword);

        // 결과 데이터 맵 구성
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);
        result.put("searchType", searchType);
        result.put("searchKeyword", searchKeyword);
        result.put("pagination", pagination); // 뷰에서 페이지 번호 출력에 사용

        return result;
    }
}
