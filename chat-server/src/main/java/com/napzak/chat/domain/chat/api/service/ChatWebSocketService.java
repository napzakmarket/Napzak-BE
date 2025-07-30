package com.napzak.chat.domain.chat.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.napzak.chat.domain.chat.amqp.ChatMessageSender;
import com.napzak.chat.domain.chat.amqp.RoomCreatedSender;
import com.napzak.domain.chat.vo.RoomDateKey;
import com.napzak.domain.push.util.FcmPushSender;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.crud.chatmessage.ChatMessageSaver;
import com.napzak.domain.chat.crud.chatparticipant.ChatParticipantUpdater;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatWebSocketService {
	private final ChatMessageSaver chatMessageSaver;
	private final ChatParticipantUpdater chatParticipantUpdater;
	private final ChatMessageSender chatMessageSender;
	private final RoomCreatedSender roomCreatedSender;
	private final FcmPushSender fcmPushSender;
	private final DateMessageSender dateMessageSender;
	private final ConcurrentMap<RoomDateKey, AtomicBoolean> dateMessageSentMap = new ConcurrentHashMap<>();

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
		log.info("🧵 sendStoreMessage called: roomId={}, senderId={}, type={}, content={}", roomId, senderId, type, content);
		try {
			RoomDateKey key = new RoomDateKey(roomId, LocalDate.now());
			if (dateMessageSentMap.computeIfAbsent(key, k -> new AtomicBoolean(false)).compareAndSet(false, true)) {
				dateMessageSender.sendDateMessage(roomId);
			}
			log.info("✅ Date message check complete");

			ChatMessage storeMessage = chatMessageSaver.save(roomId, senderId, type, content, metadata);
			log.info("💾 Message saved: {}", storeMessage);

			chatParticipantUpdater.updateLastReadMessage(roomId, senderId, storeMessage.getId());
			log.info("👁 Last read message updated for senderId={}", senderId);

			chatMessageSender.sendStoreMessage(storeMessage);
			log.info("📤 Message sent to MQ");

			if (opponentNickname != null && !deviceTokens.isEmpty()) {
				log.info("📱 Sending push to opponent: {}", opponentId);
				sendChatPush(deviceTokens, opponentNickname, type, roomId, content, opponentId);
			}
		} catch (Exception e) {
			log.error("💥 Exception in sendStoreMessage: {}", e.getMessage(), e);
		}
	}

	@Transactional
	public void sendSystemMessage(Long roomId, SystemMessageType systemMessageType) {
		ChatMessage systemMessage = chatMessageSaver.saveSystemMessage(roomId, systemMessageType);
		chatMessageSender.sendServerMessage(systemMessage);
	}

	public void notifyRoomCreated(Long roomId, List<Long> participantStoreIds) {
		if (roomId == null || participantStoreIds == null || participantStoreIds.isEmpty()) {
			log.warn("Invalid parameters for room creation notification: roomId={}, participantStoreIds={}", roomId, participantStoreIds);
			return;
		}
		for (Long storeId : participantStoreIds) {
			roomCreatedSender.send(roomId, storeId);
		}
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
