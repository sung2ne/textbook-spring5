<%@ page language="java" contentType="text/html; charset=UTF-8"%>

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
            <div class="col-4">
                <%-- 프로필 --%>
                <div class="card mb-3">
                    <div class="card-header">
                        프로필
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped">
                                <tbody>
                                    <tr>
                                        <th class="col-3">아이디</th>
                                        <td class="col-9">${profile.userId}</td>
                                    </tr>
                                    <tr>
                                        <th>이름</th>
                                        <td>${profile.username}</td>
                                    </tr>
                                    <tr>
                                        <th>이메일</th>
                                        <td>${profile.email}</td>
                                    </tr>
                                    <tr>
                                        <th>전화번호</th>
                                        <td>${profile.phone}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="card-footer">
                        <a href="/profile/update-profile" class="btn btn-warning">프로필 수정</a>
                        <a href="/profile/update-password" class="btn btn-danger">비밀번호 변경</a>
                        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal">회원 탈퇴</button>
                    </div>
                </div>
                <%--// 프로필 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 삭제 모달 --%>
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="deleteForm" action="/profile/delete" method="POST">
                    <%-- modal-header --%>
                    <div class="modal-header">
                        <h1 class="modal-title fs-5 text-danger" id="deleteModalModalLabel">
                            <strong>회원 탈퇴</strong>
                        </h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <%--// modal-header --%>

                    <%-- modal-body --%>
                    <div class="modal-body">
                        <div class="mb-3">
                            <p class="text-danger">탈퇴한 회원은 복구할 수 없습니다.</p>
                            <p>탈퇴를 진행하시겠습니까?</p>
                        </div>
                    </div>
                    <%--// modal-body --%>

                    <%-- modal-footer --%>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-outline-danger">회원 탈퇴</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
                    </div>
                    <%--// modal-footer --%>
                </form>
            </div>
        </div>
    </div>
    <%-- 삭제 모달 --%>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <%--// 자바스크립트 --%>
</body>
</html>
