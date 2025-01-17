package com.napzak.domain.chat.core.vo;

import com.napzak.domain.chat.core.entity.ChatMessageEntity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessage {
	private final Long id;
	private final Long chatRoomId;
	private final String content;
	private final LocalDateTime createdAt;
	private final boolean isRead;
	private final Long senderId;

	public ChatMessage(Long id, Long chatRoomId, String content, LocalDateTime createdAt, boolean isRead,
		Long senderId) {
		this.id = id;
		this.chatRoomId = chatRoomId;
		this.content = content;
		this.createdAt = createdAt;
		this.isRead = isRead;
		this.senderId = senderId;
	}

	public static ChatMessage fromEntity(ChatMessageEntity chatMessageEntity) {
		return new ChatMessage(
			chatMessageEntity.getId(),
			chatMessageEntity.getChatRoomId(),
			chatMessageEntity.getContent(),
			chatMessageEntity.getCreatedAt(),
			chatMessageEntity.getIsRead(),
			chatMessageEntity.getSenderId()
		);
	}
}