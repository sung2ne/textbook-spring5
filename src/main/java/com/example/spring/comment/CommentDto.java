package com.example.spring.comment;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDto {
    // 댓글 관련 정보
    private int id;                 // 댓글 ID (Primary Key)
    private int authPostId;         // 게시글 ID
    private String content;         // 댓글 내용
    private String userId;          // 댓글 작성자 아이디
    private Date createdAt;         // 댓글 작성 시간
    private Date updatedAt;         // 댓글 마지막 수정 시간

    // 사용자 관련 정보
    private String username;            // 사용자 이름
    private String phone;               // 전화번호
    private String email;               // 이메일 주소
}
