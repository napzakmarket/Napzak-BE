package com.napzak.domain.chat.crud.chatmessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.napzak.common.exception.NapzakException;
import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.entity.ChatMessageEntity;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.domain.chat.entity.enums.SystemMessageType;
import com.napzak.domain.chat.repository.ChatMessageRepository;
import com.napzak.domain.chat.vo.ChatMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageSaver {

	private final ChatMessageRepository chatMessageRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public ChatMessage save(Long roomId, Long senderId, MessageType type, String content, Map<String, Object> metadata) {
		ChatMessageEntity entity = ChatMessageEntity.create(roomId, senderId, type, content, toJson(metadata));
		chatMessageRepository.save(entity);

		return ChatMessage.fromEntity(entity);
	}

	@Transactional
	public ChatMessage saveSystemMessage(Long roomId, SystemMessageType systemMessageType){
		ChatMessageEntity entity = ChatMessageEntity.create(
			roomId, null, MessageType.SYSTEM, null, toJson(systemMessageType.getMetadataMap()));
		chatMessageRepository.save(entity);

		return ChatMessage.fromEntity(entity);
	}

	@Transactional
	public List<ChatMessage> broadcastSystemMessage(List<Long> roomIds, SystemMessageType systemMessageType) {
		List<ChatMessageEntity> entities = roomIds.stream()
			.map(roomId -> ChatMessageEntity.create(
				roomId, null, MessageType.SYSTEM, null,
				toJson(systemMessageType.getMetadataMap())
			))
			.toList();
		chatMessageRepository.saveAll(entities);

		return entities.stream()
			.map(ChatMessage::fromEntity)
			.toList();
	}

	@Transactional
	public ChatMessage saveDateMessage(Long roomId) {
		try {
			String messageType = MessageType.DATE.name();
			String metadata = toJson(Map.of(
				"type", "DATE",
				"date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
			));
			chatMessageRepository.insertDateMessageIfNotExists(roomId, messageType, metadata);

			return chatMessageRepository.findTodayDateMessage(roomId, messageType)
				.map(ChatMessage::fromEntity)
				.orElse(null); // 예외 대신 null 반환
		} catch (Exception e) {
			log.warn("❗ DATE 메시지 처리 실패: {}", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * metadata Map을 안전한 JSON String으로 직렬화
	 */
	private String toJson(Map<String, Object> metadata) {
		if (metadata == null) {
			return null;
		}
		try {
			return objectMapper.writeValueAsString(metadata);
		} catch (JsonProcessingException e) {
			throw new NapzakException(ChatErrorCode.INVALID_METADATA_FORMAT);
		}
	}
}