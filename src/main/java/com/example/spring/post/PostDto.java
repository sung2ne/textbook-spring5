package com.example.spring.post;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 게시글 정보를 담는 Data Transfer Object (DTO)
 * - 게시글의 제목, 내용, 작성자, 작성/수정일, 비밀번호 등 기본 정보 포함
 * - 파일 업로드 기능을 위한 MultipartFile 및 파일명 정보 포함
 */
@Data
public class PostDto {

    // 게시글 관련 정보
    private int id;                 // 게시글 ID (Primary Key)
    private String title;          // 게시글 제목
    private String content;        // 게시글 내용
    private String username;       // 게시글 작성자 이름
    private String password;       // 게시글 수정/삭제를 위한 비밀번호

    private Date createdAt;        // 게시글 작성 시간
    private Date updatedAt;        // 게시글 마지막 수정 시간

    // 첨부파일 관련 정보
    private MultipartFile uploadFile;     // 업로드된 첨부파일 (form에서 전송된 파일)
    private String fileName;              // 서버에 저장된 파일명
    private String originalFileName;      // 사용자가 업로드한 원본 파일명
    private boolean deleteFile;           // 기존 첨부파일 삭제 요청 여부 (true 시 삭제 처리)
}
