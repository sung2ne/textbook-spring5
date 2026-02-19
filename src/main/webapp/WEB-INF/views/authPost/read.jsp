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
        <%--// 네비게이션 --%>

        <%-- 페이지 제목 --%>
        <%@ include file="../base/title.jsp" %>
        <%--// 페이지 제목 --%>

        <%-- 메시지 --%>
        <%@ include file="../base/message.jsp" %>
        <%--// 메시지 --%>

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
                            아이디: ${post.userId} | 이름: ${post.username} | 전화번호: ${post.phone} | 이메일: ${post.email}
                        </div>
                        <div class="mb-3 text-muted">
                            등록일시: <fmt:formatDate value="${post.createdAt}" pattern="yyyy-MM-dd HH:mm"/> | 수정일시: <fmt:formatDate value="${post.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                        </div>
                        <c:if test="${post.fileName != null}">
                            <div class="mb-3">
                                첨부파일: <a href="/auth-posts/${post.id}/download" class="btn btn-outline-primary">${post.originalFileName}</a>
                            </div>
                        </c:if>
                        <div class="mb-3">
                            ${post.content}
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/auth-posts" class="btn btn-primary">목록</a>
                        <c:if test="${post.userId eq sessionScope.userId}">
                            <a href="/auth-posts/${post.id}/update" class="btn btn-warning">수정</a>
                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">삭제</button>
                        </c:if>
                    </div>
                </div>
                <%--// 게시글 보기 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <c:if test="${post.userId eq sessionScope.userId}">
    <%-- 삭제 모달 --%>
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="deleteForm" action="/auth-posts/${post.id}/delete" method="POST">
                    <%-- modal-header --%>
                    <div class="modal-header">
                        <h1 class="modal-title fs-5 text-danger" id="deleteModalModalLabel">
                            <strong>게시글 삭제</strong>
                        </h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <%--// modal-header --%>

                    <%-- modal-body --%>
                    <div class="modal-body">
                        <div class="mb-3">
                            <p class="text-danger">삭제된 데이터는 복구할 수 없습니다.</p>
                            <p>비밀번호를 입력해주세요.</p>
                        </div>
                        <div>
                            <input type="password" id="password" name="password" placeholder="비밀번호" class="form-control" required>
                        </div>
                    </div>
                    <%--// modal-body --%>

                    <%-- modal-footer --%>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-outline-danger">삭제</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    </div>
                    <%--// modal-footer --%>
                </form>
            </div>
        </div>
    </div>
    <%-- 삭제 모달 --%>
    </c:if>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <%--// 자바스크립트 --%>
</body>
</html>
