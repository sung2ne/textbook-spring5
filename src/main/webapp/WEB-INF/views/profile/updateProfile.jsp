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
                <%-- 프로필 수정 --%>
                <form id="updateProfileForm" action="/profile/update-profile" method="POST">
                    <div class="card mb-3">
                        <div class="card-header">
                            프로필 수정 (<span class="text-danger">*</span> 표시는 필수항목입니다.)
                        </div>
                        <div class="card-body">
                            <%-- 이름 --%>
                            <div class="mb-3">
                                <label for="username" class="form-label">이름<span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="username" name="username" placeholder="이름" value="${profile.username}">
                            </div>
                            <%--// 이름 --%>

                            <%-- 전화번호 --%>
                            <div class="mb-3">
                                <label for="phone" class="form-label">전화번호<span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="phone" name="phone" placeholder="전화번호" value="${profile.phone}">
                            </div>
                            <%--// 전화번호 --%>

                            <%-- 이메일 --%>
                            <div class="mb-3">
                                <label for="email" class="form-label">이메일<span class="text-danger">*</span></label>
                                <input type="email" class="form-control" id="email" name="email" placeholder="이메일" value="${profile.email}">
                            </div>
                            <%--// 이메일 --%>
                        </div>
                        <div class="card-footer">
                            <div>
                                <button type="submit" class="btn btn-primary">프로필 수정</button>
                                <a href="/profile/" class="btn btn-secondary">취소</a>
                            </div>
                        </div>
                    </div>
                </form>
                <%--// 프로필 수정 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <script>
        $(document).ready(function() {
            // 프로필 수정 폼 검증
            $('#updateProfileForm').validate({
                rules: {
                    username: {
                        required: true,
                        maxlength: 10
                    },
                    phone: {
                        required: true
                    },
                    email: {
                        required: true,
                        email: true,
                        maxlength: 50
                    }
                },
                messages: {
                    username: {
                        required: '이름을 입력하세요.',
                        maxlength: '이름은 최대 10자까지 가능합니다.'
                    },
                    phone: {
                        required: '전화번호를 입력하세요.'
                    },
                    email: {
                        required: '이메일을 입력하세요.',
                        email: '올바른 이메일 형식이 아닙니다.',
                        maxlength: '이메일은 최대 50자까지 가능합니다.'
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
