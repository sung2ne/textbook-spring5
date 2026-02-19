package com.example.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 인증 인터셉터 (AuthInterceptor)
 * - 컨트롤러 실행 전에 로그인 상태 및 권한을 확인
 * - /posts/**와 /auth/logout은 로그인 여부 관계없이 허용
 * - /auth/**로 접근할 때 로그인 상태이면 /posts로 리다이렉트
 * - 그 외 경로는 로그인 상태여야 접근 가능
 * - /users/** 경로는 ADMIN 권한만 접근 가능
 */
public class AuthInterceptor implements HandlerInterceptor {

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String userId = (String) request.getSession().getAttribute("userId");

        // 1. /posts/** 또는 /auth/logout 요청은 항상 허용
        if (requestUri.startsWith("/posts") || requestUri.equals("/auth/logout")) {
            return true;
        }

        // 2. /auth-posts/** 경로는 로그인된 사용자만 접근 가능
        if (requestUri.startsWith("/auth-posts")) {
            if (userId == null) {
                response.sendRedirect("/auth/login");
                return false;
            }
            return true; // 로그인된 사용자는 접근 가능
        }

        // 3. /comments/** 경로는 로그인된 사용자만 접근 가능
        if (requestUri.startsWith("/comments")) {
            if (userId == null) {
                response.sendRedirect("/auth/login");
                return false;
            }
            return true; // 로그인된 사용자는 접근 가능
        }

        // 4. /auth/** 요청 처리
        if (requestUri.startsWith("/auth")) {
            if (userId != null) {
                // 로그인된 사용자는 /posts로 리다이렉트
                response.sendRedirect("/posts");
                return false;
            }
            return true; // 로그인 안 된 사용자는 /auth 하위 경로 접근 가능
        }

        // 5. 로그인하지 않은 경우, 다른 요청은 차단
        if (userId == null) {
            response.sendRedirect("/auth/logout");
            return false;
        }

        // 6. /users/** 경로는 관리자만 접근 가능
        if (requestUri.startsWith("/users")) {
            String role = (String) request.getSession().getAttribute("role");
            if (role == null || !"ADMIN".equals(role)) {
                response.sendRedirect("/auth/logout");
                return false;
            }
        }

        // 7. 모든 조건 통과 → 요청 허용
        return true;
    }
}
