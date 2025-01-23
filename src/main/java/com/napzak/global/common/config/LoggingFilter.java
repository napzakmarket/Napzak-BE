package com.napzak.global.common.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import org.springframework.util.StopWatch;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
	private static final String IDENTIFIER = "request_id";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		MDC.put(IDENTIFIER, UUID.randomUUID().toString());
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);

		try {
			filterChain.doFilter(request, response);
		} finally {
			stopWatch.stop();
			log.info("Request Completed - Status: {}, Method: {}, URI: {}, TokenExists: {}, Processing Time: {}ms",
				response.getStatus(),
				request.getMethod(),
				request.getRequestURI(),
				tokenExists(token),
				stopWatch.getTotalTimeMillis());

			MDC.clear();
		}
	}

	private boolean tokenExists(String token) {
		return token != null && !token.isBlank();
	}
}