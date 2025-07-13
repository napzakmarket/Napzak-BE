package com.napzak.chat.domain.chat.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.chat.domain.chat.amqp.ChatMessageSender;
import com.napzak.domain.push.util.FcmPushSender;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageRetriever;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageSaver;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantUpdater;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatWebSocketService {
	private final ChatMessageRetriever chatMessageRetriever;
	private final ChatMessageSaver chatMessageSaver;
	private final ChatParticipantUpdater chatParticipantUpdater;
	private final ChatMessageSender chatMessageSender;
	private final FcmPushSender fcmPushSender;

	@Transactional
	public void sendStoreMessage(
		Long roomId,
		Long senderId,
		MessageType type,
		String content,
		Map<String, Object> metadata,
		List<String> deviceTokens,
		String opponentNickname,
		Long opponentId
	){
		sendDateMessage(roomId); // 조건 만족 시 DATE 삽입 후 broadcast

		ChatMessage storeMessage = chatMessageSaver.save(roomId, senderId, type, content, metadata);
		chatParticipantUpdater.updateLastReadMessage(roomId, senderId, storeMessage.getId());
		chatMessageSender.sendStoreMessage(storeMessage);

		if (opponentNickname != null && !deviceTokens.isEmpty()) {
			sendChatPush(deviceTokens, opponentNickname, type, roomId, content, opponentId);
		}
	}

	@Transactional
	public void sendSystemMessage(Long roomId, SystemMessageType systemMessageType) {
		ChatMessage systemMessage = chatMessageSaver.saveSystemMessage(roomId, systemMessageType);
		chatMessageSender.sendServerMessage(systemMessage);
	}

	@Transactional
	public void sendDateMessage(Long roomId) {
		boolean existsToday = chatMessageRetriever.existsDateMessageToday(roomId, LocalDate.now());
		if (existsToday) return; // 이미 당일 date message 있음 → 아무것도 안함
		ChatMessage dateMessage = chatMessageSaver.saveDateMessage(roomId);
		chatMessageSender.sendServerMessage(dateMessage);
	}

	private void sendChatPush(List<String> deviceTokens, String nickname, MessageType type, Long roomId, String content, Long opponentId) {
		String body = switch (type) {
			case TEXT -> content;
			case IMAGE -> "(사진)";
			default -> null;
		};

		if (body == null) return;

		deviceTokens.forEach(token ->
			fcmPushSender.sendMessage(
				opponentId,
				token,
				nickname,
				body,
				Map.of("type", "chat", "roomId", String.valueOf(roomId))
			)
		);
	}
}
