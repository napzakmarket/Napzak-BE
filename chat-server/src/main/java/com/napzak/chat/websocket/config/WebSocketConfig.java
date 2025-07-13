package com.napzak.chat.websocket.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Value("${spring.rabbitmq.relay.host}")
	private String relayHost;

	@Value("${spring.rabbitmq.relay.port}")
	private int relayPort;

	@Value("${spring.rabbitmq.relay.login}")
	private String relayLogin;

	@Value("${spring.rabbitmq.relay.passcode}")
	private String relayPasscode;

	private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws/v1")
			.addInterceptors(jwtHandshakeInterceptor)
			.setAllowedOriginPatterns("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableStompBrokerRelay("/topic")
			.setRelayHost(relayHost)
			.setRelayPort(relayPort)
			.setClientLogin(relayLogin)
			.setClientPasscode(relayPasscode);
		config.setApplicationDestinationPrefixes("/pub");
	}
}
