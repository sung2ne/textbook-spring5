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
            <div class="col-12">
                <%-- 게시글 등록 --%>
                <form id="createForm" action="/posts/create" method="POST">
                    <div class="card mb-3">
                        <div class="card-header">
                            게시글 등록 (<span class="text-danger">*</span> 표시는 필수항목입니다.)
                        </div>
                        <div class="card-body">   
                            <%-- 제목 --%>           
                            <div class="mb-3">
                                <label for="title" class="form-label">제목<span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="title" name="title" placeholder="제목을 입력하세요">
                            </div>
                            <%--// 제목 --%>

                            <%-- 내용 --%>
                            <div class="mb-3">
                                <label for="content" class="form-label">내용<span class="text-danger">*</span></label>
                                <textarea class="form-control" id="content" name="content" rows="5" placeholder="내용을 입력하세요"></textarea>
                            </div>
                            <%--// 내용 --%>

                            <%-- 작성자 --%>
                            <div class="mb-3">
                                <label for="username" class="form-label">작성자<span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="username" name="username" placeholder="작성자를 입력하세요">
                            </div>
                            <%--// 작성자 --%>

                            <%-- 비밀번호 --%>
                            <div class="mb-3">
                                <label for="password" class="form-label">비밀번호<span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password" name="password" placeholder="비밀번호를 입력하세요">
                            </div>     
                            <%--// 비밀번호 --%>               
                        </div>
                        <div class="card-footer">
                            <div>
                                <button type="submit" class="btn btn-primary">등록</button>
                                <a href="/posts" class="btn btn-secondary">취소</a>
                            </div>
                        </div>
                    </div>
                </form>
                <%--// 게시글 등록 --%>
            </div>
        </div>
        <%--// 페이지 내용 --%>
    </div>

    <%-- 자바스크립트 --%>
    <%@ include file="../base/script.jsp" %>
    <script>
        $(document).ready(function() {
            // 게시글 폼 검증
            $('#createForm').validate({
                rules: {
                    title: {
                        required: true,
                        minlength: 2,
                        maxlength: 100
                    },
                    content: {
                        required: true,
                        minlength: 2,
                        maxlength: 1000
                    },
                    username: {
                        required: true,
                        minlength: 2,
                        maxlength: 10
                    },
                    password: {
                        required: true,
                        minlength: 4,
                        maxlength: 20
                    }
                },
                messages: {
                    title: {
                        required: '제목을 입력하세요.',
                        minlength: '제목은 최소 2자 이상 입력하세요.',
                        maxlength: '제목은 최대 100자 이하로 입력하세요.'
                    },
                    content: {
                        required: '내용을 입력하세요.',
                        minlength: '내용은 최소 2자 이상 입력하세요.',
                        maxlength: '내용은 최대 1000자 이하로 입력하세요.'
                    },
                    username: {
                        required: '작성자를 입력하세요.',
                        minlength: '작성자는 최소 2자 이상 입력하세요.',
                        maxlength: '작성자는 최대 10자 이하로 입력하세요.'
                    },
                    password: {
                        required: '비밀번호를 입력하세요.',
                        minlength: '비밀번호는 최소 4자 이상 입력하세요.',
                        maxlength: '비밀번호는 최대 20자 이하로 입력하세요.'
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
