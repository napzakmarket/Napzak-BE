package com.napzak.api.admin.web;

import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.napzak.common.auth.jwt.provider.JwtTokenProvider;
import com.napzak.common.auth.jwt.provider.JwtValidationType;
import com.napzak.common.auth.role.enums.Role;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 어드민 SSR 페이지(/admin/**) 전용 인증 인터셉터.
 * 브라우저 쿠키(ADMIN_TOKEN)에 담긴 JWT를 검증하며, 모바일 앱이 쓰는 REST 보안 체계와는 독립적으로 동작한다.
 * 토큰이 없거나 유효하지 않거나 ADMIN 권한이 아니면 로그인 페이지로 리다이렉트한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

	public static final String ADMIN_TOKEN_COOKIE = "ADMIN_TOKEN";

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		String token = resolveToken(request);

		if (token == null || jwtTokenProvider.validateToken(token) != JwtValidationType.VALID_JWT) {
			response.sendRedirect("/admin/login");
			return false;
		}

		try {
			if (jwtTokenProvider.getRoleFromJwt(token) != Role.ADMIN) {
				response.sendRedirect("/admin/login");
				return false;
			}
		} catch (Exception e) {
			log.warn("Admin SSR token role resolution failed", e);
			response.sendRedirect("/admin/login");
			return false;
		}

		return true;
	}

	private String resolveToken(HttpServletRequest request) {
		if (request.getCookies() == null) {
			return null;
		}
		return Arrays.stream(request.getCookies())
			.filter(cookie -> ADMIN_TOKEN_COOKIE.equals(cookie.getName()))
			.map(Cookie::getValue)
			.findFirst()
			.orElse(null);
	}
}
