<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
            <div class="col-6">
                <div class="mb-3">
                    <%-- 검색 --%>
                    <form action="/users" method="get">
                        <div class="input-group">
                            <select name="searchType" class="form-select" style="width: 120px;">
                                <option value="userId" ${searchType == 'userId' ? 'selected' : ''}>아이디</option>
                                <option value="username" ${searchType == 'username' ? 'selected' : ''}>이름</option>
                                <option value="phone" ${searchType == 'phone' ? 'selected' : ''}>전화번호</option>
                                <option value="email" ${searchType == 'email' ? 'selected' : ''}>이메일</option>
                                <option value="all" <c:if test="${searchType == null}">selected</c:if>>전체</option>
                            </select>
                            <input type="text" name="searchKeyword" class="form-control" value="${searchKeyword}" placeholder="검색어를 입력하세요" style="width: 300px;">
                            <button type="submit" class="btn btn-primary">검색</button>
                            <c:if test="${searchKeyword != null}">
                                <a href="/users" class="btn btn-danger">취소</a>
                            </c:if>
                        </div>
                    </form>
                    <%--// 검색 --%>
                </div>
                <%--// 검색, 등록 버튼 --%>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <%-- 사용자 목록 --%>
                <table class="table table-striped table-hover table-bordered">
                    <thead>
                        <tr>
                            <th>권한</th>
                            <th>아이디</th>
                            <th>이름</th>
                            <th>전화번호</th>
                            <th>이메일</th>
                            <th>생성일시</th>
                            <th>수정일시</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="user">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${user.role eq 'ADMIN'}">관리자</c:when>
                                        <c:otherwise>사용자</c:otherwise>
                                    </c:choose>
                                </td>
                                <td><a href="/users/${user.userId}/">${user.userId}</a></td>
                                <td>${user.username}</td>
                                <td>${user.phone}</td>
                                <td>${user.email}</td>
                                <td><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><fmt:formatDate value="${user.updatedAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <%--// 사용자 목록 --%>

                <%-- 페이지네이션 --%>
                <nav aria-label="Page navigation">
                    <ul class="pagination justify-content-center">
                        <%-- 이전 페이지 --%>
                        <c:if test="${pagination.currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="/users?page=1">처음</a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/users?page=${pagination.currentPage - 1}">이전</a>
                            </li>
                        </c:if>
                        <%--// 이전 페이지 --%>

                        <%-- 페이지 번호 --%>
                        <c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="pageNumber">
                            <li class="page-item">
                                <a class="page-link <c:if test='${pageNumber == pagination.currentPage}'>active</c:if>" href="/users?page=${pageNumber}">${pageNumber}</a>
                            </li>
                        </c:forEach>
                        <%--// 페이지 번호 --%>

                        <%-- 다음 페이지 --%>
                        <c:if test="${pagination.currentPage < pagination.totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="/users?page=${pagination.currentPage + 1}">다음</a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="/users?page=${pagination.totalPages}">마지막</a>
                            </li>
                        </c:if>
                        <%--// 다음 페이지 --%>
                    </ul>
                </nav>
                <%--// 페이지네이션 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <%--// 자바스크립트 --%>
</body>
</html>
