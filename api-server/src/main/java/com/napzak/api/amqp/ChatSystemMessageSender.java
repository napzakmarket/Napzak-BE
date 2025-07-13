package com.napzak.api.amqp;

import static com.napzak.api.amqp.RabbitMQConfig.*;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatMessagePayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatSystemMessageSender {

	private final RabbitTemplate rabbitTemplate;

	public void sendSystemMessage(ChatMessage message) {
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
	}

	public void sendSystemMessages(List<ChatMessage> messages) {
		boolean isRead = true;
		for(ChatMessage message : messages) {
			ChatMessagePayload payload = ChatMessagePayload.from(message, isRead);
			rabbitTemplate.convertAndSend(
				RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, payload);
		}
	}
}