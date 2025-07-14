package com.napzak.chat.domain.chat.api.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.napzak.chat.domain.chat.api.service.ChatMessagePagination;
import com.napzak.common.util.TimeUtils;
import com.napzak.domain.chat.entity.enums.ChatMessageSortOption;
import com.napzak.domain.chat.vo.ChatMessage;

import jakarta.annotation.Nullable;

public record ChatMessageListResponse(
	List<ChatMessageDto> messages,
	@Nullable String cursor
) {
	public static ChatMessageListResponse from(
		ChatMessagePagination pagination,
		ChatMessageSortOption sortOption,
		Long myStoreId,
		Long opponentLastReadId
	) {
		List<ChatMessage> rawMessages = pagination.getMessages();
		List<ChatMessageDto> processedMessages = new ArrayList<>();

		boolean stopUnread = false;
		Long prevSenderId = null;

		for (ChatMessage m : rawMessages) {
			// senderId null-safe 비교
			boolean isMessageOwner = myStoreId.equals(m.getSenderId());

			boolean isRead;
			switch (m.getType()) {
				case PRODUCT, SYSTEM, DATE -> isRead = true;
				default -> {
					if (!isMessageOwner) {
						isRead = true;
					} else {
						if (opponentLastReadId == null) {
							isRead = false;
						} else if (m.getId() <= opponentLastReadId) {
							stopUnread = true;
						}
						isRead = stopUnread;
					}
				}
			}

			// senderId null-safe 비교
			boolean isProfileNeeded = m.getSenderId() != null && !isMessageOwner && !m.getSenderId().equals(prevSenderId);
			prevSenderId = m.getSenderId();

			processedMessages.add(ChatMessageDto.from(m, isRead, isMessageOwner, isProfileNeeded));
		}

		String nextCursor = pagination.generateNextCursor(sortOption);

		return new ChatMessageListResponse(processedMessages, nextCursor);
	}

	public record ChatMessageDto(
		Long messageId,
		Long senderId,
		String type,
		String content,
		Object metadata,
		String createdAt,
		boolean isRead,
		boolean isMessageOwner,
		boolean isProfileNeeded
	) {
		public static ChatMessageDto from(ChatMessage m, boolean isRead, boolean isOwner, boolean isProfileNeeded) {
			return new ChatMessageDto(
				m.getId(),
				m.getSenderId(),
				m.getType().name(),
				m.getContent(),
				m.getMetadataMap(),
				TimeUtils.formatChatMessageTime(m.getCreatedAt()),
				isRead,
				isOwner,
				isProfileNeeded
			);
		}
	}
}