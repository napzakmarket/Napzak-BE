package com.napzak.domain.chat.vo;

import com.napzak.domain.chat.entity.ChatParticipantEntity;

import lombok.Getter;

@Getter
public class ChatParticipant {

	private final Long id;
	private final Long roomId;
	private final Long storeId;
	private final Long lastReadMessageId;
	private final Boolean isExited;

	public ChatParticipant(Long id, Long roomId, Long storeId, Long lastReadMessageId, Boolean isExited) {
		this.id = id;
		this.roomId = roomId;
		this.storeId = storeId;
		this.lastReadMessageId = lastReadMessageId;
		this.isExited = isExited;
	}

	public static ChatParticipant fromEntity(ChatParticipantEntity entity) {
		return new ChatParticipant(
			entity.getId(),
			entity.getRoomId(),
			entity.getStoreId(),
			entity.getLastReadMessageId(),
			entity.getIsExited()
		);
	}
}
