package com.napzak.common.websocket.pubsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(SimpMessagingTemplate.class)
public class RedisSubscriber implements MessageListener {

	private final SimpMessagingTemplate messagingTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String payload = new String(message.getBody());
		log.info("[RedisSubscriber] 수신 메시지: {}", payload);

		// 수신 메시지를 다시 WebSocket 브로드캐스트
		messagingTemplate.convertAndSend("/topic/chat/presence", payload);
	}
}
