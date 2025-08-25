package com.napzak.chat.domain.chat.amqp;

import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.*;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatMessagePayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatMessageSender {

	private final RabbitTemplate rabbitTemplate;

	public void sendStoreMessage(ChatMessage message){
		boolean isRead = false;
		ChatMessagePayload payload = ChatMessagePayload.from(message, isRead);
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
	}

	public void sendServerMessage(ChatMessage message) {
		boolean isRead = true;
		ChatMessagePayload payload = ChatMessagePayload.from(message, isRead);
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
	}
}