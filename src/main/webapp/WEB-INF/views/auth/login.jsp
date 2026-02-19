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
                <%-- 로그인 폼  --%>
                <form id="loginForm" action="/auth/login" method="POST">
                    <div class="card">
                        <div class="card-header">
                            로그인
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="userId" class="form-label">아이디</label>
                                <input type="text" class="form-control" id="userId" name="userId" placeholder="아이디">
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">비밀번호</label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호">
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">로그인</button>
                            </div>
                        </div>
                        <div class="card-footer d-flex justify-content-between">
                            <a href="/auth/register" class="btn btn-success">회원가입</a>
                            <a href="/auth/find-user-id" class="btn btn-warning">아이디 찾기</a>
                            <a href="/auth/reset-password" class="btn btn-danger">비밀번호 초기화</a>
                        </div>
                    </div>
                </form>
                <%--// 로그인 폼 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <script>
        $(document).ready(function() {
            // 로그인 폼 검증
            $('#loginForm').validate({
                rules: {
                    userId: {
                        required: true,
                    },
                    password: {
                        required: true,
                    },
                },
                messages: {
                    userId: {
                        required: '아이디를 입력하세요.',
                    },
                    password: {
                        required: '비밀번호를 입력하세요.',
                    },
                },
                errorClass: 'is-invalid',
                validClass: 'is-valid',
                errorPlacement: function(error, element) {
                    error.addClass('invalid-feedback');
                    element.closest('.mb-3').append(error);
                },
                submitHandler: function(form) {
                    form.submit();
                }
            });
        });
    </script>
    <%--// 자바스크립트 --%>
</body>
</html>
