package com.napzak.domain.chat.vo;

import lombok.Getter;

import java.time.LocalDateTime;

import com.napzak.domain.chat.entity.ChatRoomEntity;

@Getter
public class ChatRoom {

	private final Long id;
	private final Long productId;
	private final LocalDateTime createdAt;

	public ChatRoom(Long id, Long productId, LocalDateTime createdAt) {
		this.id = id;
		this.productId = productId;
		this.createdAt = createdAt;
	}

	public static ChatRoom fromEntity(ChatRoomEntity entity) {
		return new ChatRoom(
			entity.getId(),
			entity.getProductId(),
			entity.getCreatedAt()
		);
	}
}
