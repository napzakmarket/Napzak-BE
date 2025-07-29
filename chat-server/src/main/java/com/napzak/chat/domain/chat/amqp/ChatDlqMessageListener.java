package com.napzak.chat.domain.chat.amqp;

import static com.napzak.chat.domain.chat.amqp.RabbitMQConfig.*;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.vo.ChatMessagePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatDlqMessageListener {

	private final RabbitTemplate rabbitTemplate;

	/**
	 * 최종 DLQ Listener: Main Queue → Retry Queue → Main → 실패 초과 → DLQ로 옴.
	 */
	@RabbitListener(queues = DLQ_QUEUE)
	public void handleDeadLetter(ChatMessagePayload payload) {
		log.warn("☠️ [DLQ] 최종 실패 메시지: {}", payload);

		// DLQ는 이미 재시도 3회 이상 넘은 것이므로 다시 보내지 않고 알림만.
		if (payload.retryCount() < MAX_RETRY_COUNT) {
			// 만약 비즈니스상 다시 보내야 한다면 → Retry Queue로 수동 복귀 가능.
			ChatMessagePayload retried = ChatMessagePayload.retry(payload);
			log.info("🔁 DLQ에서 수동 재시도: retryCount = {}", retried.retryCount());
			rabbitTemplate.convertAndSend(EXCHANGE_NAME, RETRY_ROUTING_KEY, retried);
			return;
		}

		log.error("😿 DLQ 재시도 초과. 운영자가 확인해야 합니다.");
		// 알람, Slack, Sentry 등 연동 포인트.
		throw new NapzakException(ChatErrorCode.MESSAGE_UNDELIVERED);
	}
}
