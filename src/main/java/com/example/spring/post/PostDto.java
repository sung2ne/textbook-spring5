package com.example.spring.post;

import java.util.Date;

/**
 * 게시글 정보를 담는 데이터 전송 객체 (DTO)
 * 데이터베이스의 posts 테이블과 1:1 대응되는 구조
 */
public class PostDto {

    private int id;             // 게시글 ID (PRIMARY KEY)
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private String username;    // 게시글 작성자 이름
    private String password;    // 게시글 수정/삭제를 위한 비밀번호
    private Date createdAt;     // 게시글 작성 시간
    private Date updatedAt;     // 게시글 마지막 수정 시간

    // Getter 및 Setter 메서드: 각 필드에 대한 값을 가져오거나 설정할 때 사용

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
