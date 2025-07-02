package com.napzak.global.auth.jwt.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.napzak.domain.store.core.entity.enums.Role;
import com.napzak.global.auth.jwt.provider.JwtTokenProvider;
import com.napzak.global.auth.jwt.provider.JwtValidationType;
import com.napzak.global.auth.security.AdminAuthentication;
import com.napzak.global.auth.security.MemberAuthentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String token = getJwtFromRequest(request);

		if (!StringUtils.hasText(token)) {
			log.info("JWT Token not found in request header. Assuming guest access or public API request.");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			JwtValidationType validationType = jwtTokenProvider.validateToken(token);

			if (validationType == JwtValidationType.VALID_JWT) {
				setAuthentication(token, request);
				filterChain.doFilter(request, response);
			} else {
				handleInvalidToken(validationType, response);
			}
		} catch (Exception e) {
			log.error("JWT Authentication Exception: ", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 응답
		}
	}

	private void setAuthentication(String token, HttpServletRequest request) {
		Long storeId = jwtTokenProvider.getStoreIdFromJwt(token);
		Role role = jwtTokenProvider.getRoleFromJwt(token);

		log.info("Setting authentication for storeId: {} with role: {}", storeId, role);

		Collection<GrantedAuthority> authorities = List.of(role.toGrantedAuthority());
		UsernamePasswordAuthenticationToken authentication = createAuthentication(storeId, authorities, role);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		log.info("Authentication set: storeId: {}, role: {}", storeId, role);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void handleInvalidToken(JwtValidationType validationType, HttpServletResponse response) {
		if (validationType == JwtValidationType.EXPIRED_JWT_TOKEN) {
			log.warn("JWT Token is expired");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답
		} else {
			log.warn("JWT Token is invalid");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 응답
		}
	}

	private UsernamePasswordAuthenticationToken createAuthentication(Long storeId,
		Collection<GrantedAuthority> authorities, Role role) {
		log.info("Creating authentication for storeId: {} with role: {}", storeId, role);

		if (role == Role.ADMIN) {
			log.info("Creating AdminAuthentication for storeId: {}", storeId);
			return new AdminAuthentication(storeId.toString(), null, authorities);
		} else {
			log.info("Creating MemberAuthentication for storeId: {}", storeId);
			return new MemberAuthentication(storeId.toString(), null, authorities);
		}
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		return null;
	}
}