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
                        <c:if test="${post.secret eq 'Y'}">
                            <span class="badge text-bg-danger">비밀글</span>&nbsp;
                        </c:if>
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

                <%-- 댓글 등록 --%>
                <form id="commentCreateForm">
                    <div class="card mb-3">
                        <div class="card-body">
                            <%-- 내용 --%>
                            <div class="mb-3">
                                <textarea class="form-control" id="createContent" name="createContent" rows="5" placeholder="댓글 내용을 입력하세요"></textarea>
                            </div>
                            <%--// 내용 --%>
                        </div>
                        <div class="card-footer">
                            <div>
                                <button type="submit" class="btn btn-primary">댓글 등록</button>
                                <button type="button" class="btn btn-secondary" id="cancelCreateComment">등록 취소</button>
                            </div>
                        </div>
                    </div>
                </form>
                <%--// 댓글 등록 --%>

                <%-- 댓글 목록 --%>
                <div id="commentsDiv">
                </div>
                <%--// 댓글 목록 --%>
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
    <script>
        $(document).ready(function() {
            // 날짜 형식 변환
            function formatDate(timestamp) {
                const date = new Date(Number(timestamp));
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                const hour = String(date.getHours()).padStart(2, '0');
                const minute = String(date.getMinutes()).padStart(2, '0');
                return year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
            }

            // 줄바꿈 처리 함수
            function replaceNewLines(text) {
                if (!text) return '';
                return text.replace(/\n/g, '<br>');
            }

            // 댓글 목록 불러오기 함수
            function getComments() {
                $.ajax({
                    url: '/comments',
                    type: 'GET',
                    data: {
                        'authPostId': '${post.id}'
                    },
                    success: function(response) {
                        let comments = response.comments;
                        let commentsDiv = '';
                        for (let comment of comments) {
                            commentsDiv += '<div class="card mb-3" id="card' + comment.id + '">';
                            commentsDiv += '<div class="card-body">';
                            commentsDiv += '<div class="mb-3 text-muted">';
                            commentsDiv += '아이디: ' + comment.userId + ' | 이름: ' + comment.username + ' | 전화번호: ' + comment.phone + ' | 이메일: ' + comment.email;
                            commentsDiv += '</div>';
                            commentsDiv += '<div class="mb-3 text-muted">';
                            commentsDiv += '등록일시: ' + formatDate(comment.createdAt) + ' | 수정일시: ' + formatDate(comment.updatedAt);
                            commentsDiv += '</div>';
                            commentsDiv += '<div class="mb-3">';
                            commentsDiv += replaceNewLines(comment.content);
                            commentsDiv += '</div>';
                            commentsDiv += '</div>';

                            // 댓글 수정, 삭제 버튼
                            if (comment.userId == '${sessionScope.userId}') {
                                commentsDiv += '<div class="card-footer">';
                                commentsDiv += '<button type="button" class="btn btn-warning me-1 btn-update-comment" data-id=' + comment.id + '>댓글 수정</button>';
                                commentsDiv += '<button type="button" class="btn btn-danger btn-delete-comment" data-id=' + comment.id + '>댓글 삭제</button>';
                                commentsDiv += '</div>';
                            }

                            commentsDiv += '</div>';

                            // 댓글 수정 폼
                            commentsDiv += '<form class="comment-update-form d-none" id="commentUpdateForm' + comment.id + '" data-id="' + comment.id + '">';
                            commentsDiv += '<div class="card mb-3">';
                            commentsDiv += '<div class="card-body">';
                            commentsDiv += '<div class="mb-3">';
                            commentsDiv += '<textarea class="form-control" id="updateContent' + comment.id + '" name="updateContent' + comment.id + '" rows="5" placeholder="댓글 내용을 입력하세요">' + comment.content + '</textarea>';
                            commentsDiv += '</div>';
                            commentsDiv += '</div>';
                            commentsDiv += '<div class="card-footer">';
                            commentsDiv += '<div>';
                            commentsDiv += '<button type="submit" class="btn btn-warning me-2">댓글 수정</button>';
                            commentsDiv += '<button type="button" class="btn btn-secondary cancel-update-comment" data-id="' + comment.id + '">수정 취소</button>';
                            commentsDiv += '</div>';
                            commentsDiv += '</div>';
                            commentsDiv += '</div>';
                            commentsDiv += '</form>';
                        }

                        $('#commentsDiv').empty();
                        $('#commentsDiv').html(commentsDiv);
                        $('#createContent').val('');

                        // 각 수정 폼에 validate 적용
                        $('.comment-update-form').each(function() {
                            const formId = $(this).attr('id');
                            const commentId = $(this).data('id');

                            $(this).validate({
                                rules: {
                                    ['updateContent' + commentId]: {
                                        required: true,
                                        minlength: 2,
                                        maxlength: 1000
                                    }
                                },
                                messages: {
                                    ['updateContent' + commentId]: {
                                        required: '댓글을 입력하세요.',
                                        minlength: '댓글은 최소 2자 이상 입력하세요.',
                                        maxlength: '댓글은 최대 1000자 이하로 입력하세요.'
                                    }
                                },
                                errorClass: 'is-invalid',
                                validClass: 'is-valid',
                                errorPlacement: function(error, element) {
                                    error.addClass('invalid-feedback');
                                    element.closest('.mb-3').append(error);
                                },
                                submitHandler: function(form) {
                                    $.ajax({
                                        url: '/comments/update',
                                        type: 'POST',
                                        data: {
                                            'id': commentId,
                                            'content': $('#updateContent' + commentId).val()
                                        },
                                        success: function(response) {
                                            if (response.result == 'ok') {
                                                getComments();
                                                alert('댓글이 수정되었습니다.');
                                            } else {
                                                alert('댓글 수정에 실패했습니다.');
                                            }
                                        },
                                        error: function(xhr, status, error) {
                                            console.error('Error:', error);
                                            alert('댓글 수정 중 오류가 발생했습니다. 다시 시도해주세요.');
                                        }
                                    });
                                }
                            });
                        });
                    },
                    error: function(xhr, status, error) {
                        console.error('Error:', error);
                        alert('댓글 목록 불러오기 중 오류가 발생했습니다. 다시 시도해주세요.');
                    }
                });
            }

            // 댓글 목록 불러오기
            getComments();

            // 댓글 등록 취소
            $('#cancelCreateComment').on('click', function() {
                $('#createContent').val('');
            });

            // 댓글 등록 폼 검증
            $('#commentCreateForm').validate({
                rules: {
                    createContent: {
                        required: true,
                        minlength: 2,
                        maxlength: 1000
                    }
                },
                messages: {
                    createContent: {
                        required: '댓글을 입력하세요.',
                        minlength: '댓글은 최소 2자 이상 입력하세요.',
                        maxlength: '댓글은 최대 1000자 이하로 입력하세요.'
                    }
                },
                errorClass: 'is-invalid',
                validClass: 'is-valid',
                errorPlacement: function(error, element) {
                    error.addClass('invalid-feedback');
                    element.closest('.mb-3').append(error);
                },
                submitHandler: function(form) {
                    $.ajax({
                        url: '/comments/create',
                        type: 'POST',
                        data: {
                            'authPostId': '${post.id}',
                            'content': $('#createContent').val()
                        },
                        success: function(response) {
                            if (response.result == 'ok') {
                                $('#createContent').val('');
                                getComments();
                                alert('댓글이 등록되었습니다.');
                            } else {
                                alert('댓글 등록에 실패했습니다.');
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error('Error:', error);
                            alert('댓글 등록 중 오류가 발생했습니다. 다시 시도해주세요.');
                        }
                    });
                }
            });

            // 댓글 수정 버튼 클릭 이벤트
            $(document).on('click', '.btn-update-comment', function() {
                let id = $(this).data('id');
                $('#card' + id).addClass('d-none');
                $('#commentUpdateForm' + id).removeClass('d-none');
            });

            // 댓글 수정 취소 버튼 클릭 이벤트
            $(document).on('click', '.cancel-update-comment', function() {
                let id = $(this).data('id');
                $('#card' + id).removeClass('d-none');
                $('#commentUpdateForm' + id).addClass('d-none');
            });

            // 댓글 삭제 버튼 클릭 이벤트
            $(document).on('click', '.btn-delete-comment', function() {
                if (confirm('정말 삭제하시겠습니까?')) {
                    let id = $(this).data('id');

                    $.ajax({
                        url: '/comments/delete',
                        type: 'POST',
                        data: {
                            'id': id
                        },
                        success: function(response) {
                            if (response.result == 'ok') {
                                getComments();
                                alert('댓글이 삭제되었습니다.');
                            } else {
                                alert('댓글 삭제에 실패했습니다.');
                            }
                        },
                        error: function(xhr, status, error) {
                            console.error('Error:', error);
                            alert('댓글 삭제 중 오류가 발생했습니다. 다시 시도해주세요.');
                        }
                    });
                }
            });
        });
    </script>
    <%--// 자바스크립트 --%>
</body>
</html>
