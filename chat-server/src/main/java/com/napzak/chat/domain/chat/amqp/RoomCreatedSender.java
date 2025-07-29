package com.napzak.chat.domain.chat.amqp;

import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.EXCHANGE_NAME;
import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.ROOM_CREATED_ROUTING_KEY;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.chat.vo.RoomCreatedPayload;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomCreatedSender {
	private final RabbitTemplate rabbitTemplate;

	public void send(Long roomId, Long storeId) {
		RoomCreatedPayload payload = RoomCreatedPayload.from(roomId, storeId);
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROOM_CREATED_ROUTING_KEY, payload);
	}
}