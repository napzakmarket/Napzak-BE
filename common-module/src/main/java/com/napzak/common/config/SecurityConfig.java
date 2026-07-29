package com.napzak.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.napzak.common.auth.jwt.filter.JwtAuthenticationFilter;
import com.napzak.common.auth.role.enums.Role;
import com.napzak.common.auth.security.CustomAccessDeniedHandler;
import com.napzak.common.auth.security.CustomJwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomJwtAuthenticationEntryPoint customJwtAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	private static final String[] AUTH_WHITELIST = {
		"/health-check",
		"/actuator/health",
		"/v3/api-docs/**",
		"/swagger-ui/**",
		"/swagger-resources/**",
		"/api/v1/files/**",
		"/api/v1/presigned-url/**",
		"/error",
		"/api/v1/stores/login/**",
		"/api/v1/admin/login",
		"/css/**",
		"/js/**",
		"/images/**",
		"/api/v1/onboarding/**",
		"/api/v1/stores/refresh-token/**",
		"/api/v1/stores/terms/**",
		"/ws/v1/**",
		"/",
		"/stomp-test.html",
		"/ws-test.html",
		"/favicon.ico",
		"/product/**",
		"/.well-known/**"
	};

	private static final String[] AUTH_ADMIN_ONLY = {
		"/api/v1/admin/**"
	};

	/**
	 * 어드민 SSR 전용 체인 - CSRF 보호 활성화
	 */
	@Bean
	@Order(1)
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/admin/**")
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.csrf(csrf ->
				csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

		return http.build();
	}

	/**
	 * 모바일/REST 전용 체인 - CSRF 보호 비활성화
	 */
	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception ->
				exception.authenticationEntryPoint(customJwtAuthenticationEntryPoint)
					.accessDeniedHandler(customAccessDeniedHandler));

		http.authorizeHttpRequests(auth ->
				auth.requestMatchers(AUTH_WHITELIST).permitAll()
					.requestMatchers(AUTH_ADMIN_ONLY).hasAuthority(Role.ADMIN.getRoleName())
					.anyRequest().authenticated())
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
