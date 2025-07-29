package com.napzak.chat.domain.chat.amqp;

import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.vo.RoomCreatedPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCreatedListener {
	private final SimpMessagingTemplate messagingTemplate;
	private final RabbitTemplate rabbitTemplate;

	@RabbitListener(queues = ROOM_CREATED_QUEUE)
	public void handle(RoomCreatedPayload payload) {
		try {
			String destination = "/queue/chat.room-created." + payload.storeId();
			messagingTemplate.convertAndSend(destination, payload.roomId().toString());
			log.info("🚀 Notified store {} about new room {}", payload.storeId(), payload.roomId());
		} catch (Exception e) {
			if (payload.retryCount() < MAX_RETRY_COUNT) {
				RoomCreatedPayload retried = RoomCreatedPayload.retry(payload);
				rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROOM_CREATED_RETRY_ROUTING_KEY, retried);
				log.warn("🔁 Retrying RoomCreatedPayload: {}", retried);
				return;
			}
			log.error("❌ Failed to deliver RoomCreatedPayload after retries: {}", payload, e);
			throw new NapzakException(ChatErrorCode.MESSAGE_UNDELIVERED);
		}
	}
}
