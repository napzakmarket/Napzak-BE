package com.napzak.chat.websocket.config;

import com.napzak.common.auth.context.StoreSession;
import com.napzak.common.auth.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean beforeHandshake(
		ServerHttpRequest request,
		ServerHttpResponse response,
		WebSocketHandler wsHandler,
		Map<String, Object> attributes
	) {
		String token = getTokenFromRequest(request);

		if (token == null || jwtTokenProvider.validateToken(token).isNotValid()) {
			log.warn("Invalid or missing JWT token during WebSocket handshake");
			return false;
		}

		Long storeId = jwtTokenProvider.getStoreIdFromJwt(token);
		var role = jwtTokenProvider.getRoleFromJwt(token);

		StoreSession storeSession = StoreSession.of(storeId, role);

		attributes.put("storeSession", storeSession);

		log.info("WebSocket handshake succeeded: storeId={}, role={}", storeId, role);

		return true;
	}

	@Override
	public void afterHandshake(
		ServerHttpRequest request,
		ServerHttpResponse response,
		WebSocketHandler wsHandler,
		Exception exception) {
		// No-op
	}

	private String getTokenFromRequest(ServerHttpRequest request) {
		var headers = request.getHeaders();
		String authHeader = headers.getFirst("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		List<String> tokenParams = UriComponentsBuilder.fromUri(request.getURI())
			.build()
			.getQueryParams()
			.get("token");

		return (tokenParams != null && !tokenParams.isEmpty()) ? tokenParams.get(0) : null;
	}
}
