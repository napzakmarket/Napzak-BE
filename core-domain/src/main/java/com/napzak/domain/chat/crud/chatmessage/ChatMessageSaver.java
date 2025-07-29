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
		String messageType = MessageType.DATE.name();
		String metadata = toJson(Map.of(
			"type", "DATE",
			"date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))
		));
		chatMessageRepository.insertDateMessageIfNotExists(roomId, messageType, metadata);
		ChatMessageEntity entity = chatMessageRepository.findTodayDateMessage(roomId, messageType)
			.orElseThrow(() -> new NapzakException(ChatErrorCode.MESSAGE_NOT_FOUND));
		return ChatMessage.fromEntity(entity);
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