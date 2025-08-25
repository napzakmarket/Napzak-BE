package com.napzak.chat.domain.chat.amqp;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.chat.vo.ChatMessagePayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PresenceMessageSender {
	private final SimpMessagingTemplate simpMessagingTemplate;

	public void sendPresenceMessage(Long roomId, Long userId, MessageType type) {
		ChatMessagePayload payload = new ChatMessagePayload(
			null, // messageId 없음
			roomId,
			userId,
			type,
			null,
			null,
			null,
			null,
			0
		);
		simpMessagingTemplate.convertAndSend("/topic/chat.room." + roomId, payload);
	}

}
