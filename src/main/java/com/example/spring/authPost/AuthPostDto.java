package com.example.spring.authPost;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AuthPostDto {

    // 게시글 관련 정보
    private int id;                     // 게시글 ID (Primary Key)
    private String title;               // 게시글 제목
    private String content;             // 게시글 내용
    private String userId;              // 사용자 ID
    private String secret;              // 비밀글 여부(Y, N)

    private Date createdAt;             // 게시글 작성 시간
    private Date updatedAt;             // 게시글 마지막 수정 시간

    // 첨부파일 관련 정보
    private MultipartFile uploadFile;   // 업로드된 첨부파일 (form에서 전송된 파일)
    private String fileName;            // 서버에 저장된 파일명
    private String originalFileName;    // 사용자가 업로드한 원본 파일명
    private boolean deleteFile;         // 기존 첨부파일 삭제 요청 여부 (true 시 삭제 처리)

    // 사용자 관련 정보
    private String username;            // 사용자 이름
    private String phone;               // 전화번호
    private String email;               // 이메일 주소
    private String role;                // 사용자 구분(USER, ADMIN)
}
