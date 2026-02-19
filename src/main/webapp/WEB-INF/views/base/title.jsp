<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty pageTitle}">
    <div class="row mt-3">
        <div class="col-12">
            <h4>${pageTitle}</h4>
            <hr>
        </div>
    </div>
</c:if>
