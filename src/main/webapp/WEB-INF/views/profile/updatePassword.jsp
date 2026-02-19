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
                <%-- 비밀번호 수정 --%>
                <form id="updatePasswordForm" action="/profile/update-password" method="POST">
                    <div class="card mb-3">
                        <div class="card-header">
                            비밀번호 수정 (<span class="text-danger">*</span> 표시는 필수항목입니다.)
                        </div>
                        <div class="card-body">
                            <%-- 비밀번호 --%>
                            <div class="mb-3">
                                <label for="password" class="form-label">비밀번호<span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호">
                            </div>
                            <%--// 비밀번호 --%>

                            <%-- 비밀번호 확인 --%>
                            <div class="mb-3">
                                <label for="password2" class="form-label">비밀번호 확인<span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password2" name="password2" placeholder="비밀번호 확인">
                            </div>
                            <%--// 비밀번호 확인 --%>
                        </div>
                        <div class="card-footer">
                            <div>
                                <button type="submit" class="btn btn-primary">비밀번호 수정</button>
                                <a href="/profile/" class="btn btn-secondary">취소</a>
                            </div>
                        </div>
                    </div>
                </form>
                <%--// 비밀번호 수정 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <script>
        $(document).ready(function() {
            // 비밀번호 수정 폼 검증
            $('#updatePasswordForm').validate({
                rules: {
                    password: {
                        required: true,
                        minlength: 8,
                        maxlength: 20
                    },
                    password2: {
                        required: true,
                        equalTo: '#password'
                    }
                },
                messages: {
                    password: {
                        required: '비밀번호를 입력하세요.',
                        minlength: '비밀번호는 8자 이상 20자 이하로 입력하세요.',
                        maxlength: '비밀번호는 8자 이상 20자 이하로 입력하세요.'
                    },
                    password2: {
                        required: '비밀번호 확인을 입력하세요.',
                        equalTo: '비밀번호가 일치하지 않습니다.'
                    }
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
