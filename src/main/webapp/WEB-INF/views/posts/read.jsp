<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% pageContext.setAttribute("newLine", "\n"); %>

<!DOCTYPE html>
<html lang="ko">
<%@ include file="../base/head.jsp" %>
<body>
    <div class="container">
        <%-- 네비게이션 --%>
        <%@ include file="../base/navbar.jsp" %>
        <%-- // 네비게이션 --%>

        <%-- 페이지 제목 --%>
        <%@ include file="../base/title.jsp" %>
        <%-- // 페이지 제목 --%>

        <%-- 메시지 --%>
        <%@ include file="../base/message.jsp" %>
        <%-- // 메시지 --%>

        <%-- 페이지 내용 --%>
        <div class="row">
            <div class="col-12">
                <%-- 게시글 보기 --%>
                <div class="card mb-3">
                    <div class="card-header">
                        <strong>${post.title}</strong>
                    </div>
                    <div class="card-body">  
                        <div class="mb-3 text-muted">
                            글쓴이: ${post.username} | 등록일시: <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm"/> | 수정일시: <fmt:formatDate value="${post.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                        </div>
                        <div class="mb-3">
                            ${fn:replace(post.content, newLine, "<br>")}
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/posts" class="btn btn-primary">목록</a>
                        <a href="/posts/${post.id}/update" class="btn btn-warning">수정</a>
                        <button type="button" class="btn btn-danger">삭제</button>
                    </div>
                </div>        
                <%-- // 게시글 보기 --%>
            </div>
        </div>
        <%-- // 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <%-- // 자바스크립트 --%>
</body>
</html>
