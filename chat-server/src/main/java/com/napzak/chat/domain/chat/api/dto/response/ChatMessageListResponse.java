package com.napzak.chat.domain.chat.api.dto.response;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.napzak.chat.domain.chat.api.service.ChatMessagePagination;
import com.napzak.common.util.TimeUtils;
import com.napzak.domain.chat.entity.enums.ChatMessageSortOption;
import com.napzak.domain.chat.entity.enums.MessageType;
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
		List<ChatMessage> rawMessages = pagination.getMessages(); // 최신순 정렬
		Deque<ChatMessageDto> resultDeque = new ArrayDeque<>(); // addFirst로 최신순 유지

		Long prevSenderId = null;
		boolean datePassed = false;  // DATE 메시지 이후 첫 메시지 플래그
		boolean productPassed = false; // PRODUCT 메시지 이후 첫 메시지 플래그
		boolean isReadAllowed = true; // 처음엔 true, 조건 만족하면 false

		// 시간순으로 순회 (역방향)
		for (int i = rawMessages.size() - 1; i >= 0; i--) {
			ChatMessage message = rawMessages.get(i);
			Long senderId = message.getSenderId();
			boolean isOwner = myStoreId.equals(senderId);

			// isRead 계산
			boolean isRead = switch (message.getType()) {
				case PRODUCT, SYSTEM, DATE -> true;
				default -> {
					if (!isOwner) yield true;
					if (opponentLastReadId == null) yield false;
					if (message.getId() > opponentLastReadId) isReadAllowed = false;
					yield isReadAllowed;
				}
			};

			// DATE 만나면 플래그 설정
			if (message.getType() == MessageType.DATE) {
				prevSenderId = null;
				datePassed = true;
			}

			boolean isProfileNeeded = false;

			if (message.getType() != MessageType.DATE) {
				if (datePassed) {
					isProfileNeeded = true;     //  DATE 뒤 첫 메시지는 무조건 true
				} else if (senderId != null && !senderId.equals(prevSenderId)) {
					isProfileNeeded = true;     //  sender 바뀐 경우도 true
				} else if (productPassed) {
					isProfileNeeded = true;     //  PRODUCT 뒤 첫 메시지는 무조건 true
					productPassed = false;
				}

				if (senderId != null) {
					prevSenderId = senderId;
				}

				datePassed = false;             //  DATE 이후 첫 메시지 처리 완료
			}

			// PRODUCT 만나면 플래그 설정
			if (message.getType() == MessageType.PRODUCT) {
				productPassed = true;
			}

			// addFirst로 최신순 정렬 유지
			resultDeque.addFirst(ChatMessageDto.from(message, isRead, isOwner, isProfileNeeded));
		}

		String nextCursor = pagination.generateNextCursor(sortOption);
		return new ChatMessageListResponse(new ArrayList<>(resultDeque), nextCursor);
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