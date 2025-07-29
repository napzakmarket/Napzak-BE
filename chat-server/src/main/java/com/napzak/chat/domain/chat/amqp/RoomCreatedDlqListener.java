package com.napzak.chat.domain.chat.amqp;

import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.napzak.domain.chat.vo.RoomCreatedPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCreatedDlqListener {
	private final RabbitTemplate rabbitTemplate;

	@RabbitListener(queues = ROOM_CREATED_DLQ_QUEUE)
	public void handle(RoomCreatedPayload payload) {
		log.warn("☠️ [DLQ] RoomCreatedPayload failed: {}", payload);
		if (payload.retryCount() < MAX_RETRY_COUNT) {
			RoomCreatedPayload retried = RoomCreatedPayload.retry(payload);
			rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROOM_CREATED_RETRY_ROUTING_KEY, retried);
			log.info("🔁 DLQ retrying RoomCreatedPayload: {}", retried);
		} else {
			log.error("❌ DLQ retry exceeded for RoomCreatedPayload: {}", payload);
			// Optional: Sentry, Slack, alert
		}
	}
}
