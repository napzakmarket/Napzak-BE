package com.napzak.domain.chat.vo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.napzak.common.util.TimeUtils;
import com.napzak.domain.chat.entity.enums.MessageType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatMessagePayload(
	 Long messageId,
	 Long roomId,
	 Long senderId,
	 MessageType type,
	 String content,
	 Map<String, Object> metadata,
	 String createdAt,
	 Boolean isRead,
	 int retryCount
){
	// 최초 메세지 생성
	public static ChatMessagePayload from(ChatMessage chatMessage, boolean isRead) {
		return new ChatMessagePayload(
			chatMessage.getId(),
			chatMessage.getRoomId(),
			chatMessage.getSenderId(),
			chatMessage.getType(),
			chatMessage.getContent(),
			chatMessage.getMetadataMap(),
			TimeUtils.formatChatMessageTime(chatMessage.getCreatedAt()),
			isRead,
			0
		);
	}

	// DLQ 재시도 전용 팩토리
	public static ChatMessagePayload retry(ChatMessagePayload payload){
		return new ChatMessagePayload(
			payload.messageId(),
			payload.roomId(),
			payload.senderId(),
			payload.type(),
			payload.content(),
			payload.metadata(),
			payload.createdAt(),
			payload.isRead(),
			payload.retryCount() + 1
		);
	}
}