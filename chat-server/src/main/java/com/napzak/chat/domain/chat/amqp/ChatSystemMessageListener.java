package com.napzak.chat.domain.chat.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.vo.ChatMessagePayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSystemMessageListener {

	private final SimpMessagingTemplate messagingTemplate;

	@RabbitListener(queues = RabbitMQConfig.MAIN_QUEUE)
	public void handleMessage(ChatMessagePayload payload){
		try {
			log.info("📩 Received message: {}", payload);
			String destination = "/topic/chat.room." + payload.roomId();
			messagingTemplate.convertAndSend(destination, payload);
		} catch (Exception e) {
			throw new NapzakException(ChatErrorCode.MESSAGE_UNDELIVERED);
		}
	}
}