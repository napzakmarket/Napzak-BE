package com.napzak.domain.chat.vo;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

import com.napzak.domain.chat.code.ChatErrorCode;
import com.napzak.domain.chat.entity.ChatMessageEntity;
import com.napzak.domain.chat.entity.enums.MessageType;
import com.napzak.common.exception.NapzakException;

@Getter
public class ChatMessage {

	private final Long id;
	private final Long roomId;
	private final Long senderId;
	private final MessageType type;
	private final String content;
	private final String metadata;
	private final LocalDateTime createdAt;

	public ChatMessage(Long id, Long roomId, Long senderId,
		MessageType type, String content, String metadata,
		LocalDateTime createdAt) {
		this.id = id;
		this.roomId = roomId;
		this.senderId = senderId;
		this.type = type;
		this.content = content;
		this.metadata = metadata;
		this.createdAt = createdAt;
	}

	// JSON String → Map 으로 변환해서 반환
	public Map<String, Object> getMetadataMap() {
		if (metadata == null) {
			return null;
		}
		try {
			return new com.fasterxml.jackson.databind.ObjectMapper()
				.readValue(metadata, new com.fasterxml.jackson.core.type.TypeReference<>() {});
		} catch (Exception e) {
			throw new NapzakException(ChatErrorCode.INVALID_METADATA_FORMAT);
		}
	}

	public static ChatMessage fromEntity(ChatMessageEntity entity) {
		return new ChatMessage(
			entity.getId(),
			entity.getRoomId(),
			entity.getSenderId(),
			entity.getType(),
			entity.getContent(),
			entity.getMetadata(),
			entity.getCreatedAt()
		);
	}

	// public static ChatMessage empty(Long roomId) {
	// 	return new ChatMessage(
	// 		null,
	// 		roomId,
	// 		null,
	// 		MessageType.TEXT,
	// 		"",
	// 		null,
	// 		LocalDateTime.now()
	// 	);
	// }
}