package com.example.spring.post;

import java.util.Date;

import lombok.Data;

@Data
public class PostDto {

    private int id;             // 게시글 ID (Primary Key)
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private String username;    // 게시글 작성자 이름
    private String password;    // 게시글 수정/삭제를 위한 비밀번호
    private Date createdAt;     // 게시글 작성 시간
    private Date updatedAt;     // 게시글 마지막 수정 시간
}
