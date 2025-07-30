package com.napzak.chat.domain.chat.api.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.chat.domain.chat.amqp.ChatMessageSender;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageSaver;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DateMessageSender {
	private final ChatMessageRetriever chatMessageRetriever;
	private final ChatMessageSaver chatMessageSaver;
	private final ChatMessageSender chatMessageSender;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void sendDateMessage(Long roomId) {
		boolean existsToday = chatMessageRetriever.existsDateMessageToday(roomId, LocalDate.now());
		if (existsToday) return; // 이미 당일 date message 있음 → 아무것도 안함
		try {
			ChatMessage dateMessage = chatMessageSaver.saveDateMessage(roomId);
			if (dateMessage != null) {
				chatMessageSender.sendServerMessage(dateMessage);
				log.info("📅 Date message sent: {}", dateMessage.getId());
			} else {
				log.info("📭 No date message to send (possibly duplicate)");
			}
		} catch (Exception e) {
			log.warn("❗ DATE 메시지 처리 실패: {}", e.getMessage(), e);
		}
	}
}
