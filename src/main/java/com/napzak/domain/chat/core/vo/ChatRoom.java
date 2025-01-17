package com.napzak.domain.chat.core.vo;

import com.napzak.domain.chat.core.entity.ChatRoomEntity;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoom {
	private final Long id;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;
	private final Long productId;
	private final Long ownerId;
	private final Long requesterId;

	public ChatRoom(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, Long productId, Long ownerId,
		Long requesterId) {
		this.id = id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.productId = productId;
		this.ownerId = ownerId;
		this.requesterId = requesterId;
	}

	public static ChatRoom fromEntity(ChatRoomEntity chatRoomEntity) {
		return new ChatRoom(
			chatRoomEntity.getId(),
			chatRoomEntity.getCreatedAt(),
			chatRoomEntity.getUpdatedAt(),
			chatRoomEntity.getProductId(),
			chatRoomEntity.getOwnerId(),
			chatRoomEntity.getRequesterId()
		);
	}
}