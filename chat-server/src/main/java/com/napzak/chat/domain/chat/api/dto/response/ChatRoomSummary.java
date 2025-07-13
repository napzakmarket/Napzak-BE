package com.napzak.chat.domain.chat.api.dto.response;

import static com.napzak.common.auth.role.enums.Role.*;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.napzak.common.util.TimeUtils;
import com.napzak.domain.chat.vo.ChatMessage;
import com.napzak.domain.chat.vo.ChatParticipant;
import com.napzak.domain.store.vo.Store;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ChatRoomSummary(
	Long roomId,
	String opponentNickname,
	boolean isOpponentWithdrawn,
	String lastMessage,
	String lastMessageAt,
	int unreadCount,
	String opponentStorePhoto,
	@JsonIgnore
	LocalDateTime createdAt
) {
	public static ChatRoomSummary from(
		ChatParticipant myRoom,
		Store opponent,
		Map<Long, ChatMessage> lastMessages,
		Map<Long, Long> unreadCounts
	) {
		boolean isWithdrawn = opponent.getRole().equals(WITHDRAWN);
		String nickname = isWithdrawn ? "(탈퇴한 사용자) " + opponent.getNickname() : opponent.getNickname();

		ChatMessage lastMessage = lastMessages.get(myRoom.getRoomId());
		if (lastMessage == null) {
			log.warn("💥 lastMessage is null for roomId: {}", myRoom.getRoomId());
		}

		String lastMessageContent = "";
		if (lastMessage != null) {
			switch (lastMessage.getType()) {
				case TEXT -> lastMessageContent = lastMessage.getContent();
				case IMAGE -> lastMessageContent = "(사진)";
				case SYSTEM -> {
					Map<String, Object> metadataMap = lastMessage.getMetadataMap();
					lastMessageContent = metadataMap.getOrDefault("content", "").toString();
				}
				default -> lastMessageContent = "";
			}
		}

		LocalDateTime createdAt = lastMessage != null ? lastMessage.getCreatedAt() : null;

		return new ChatRoomSummary(
			myRoom.getRoomId(),
			nickname,
			isWithdrawn,
			lastMessageContent,
			TimeUtils.calculateChatRoomTime(createdAt),
			unreadCounts.getOrDefault(myRoom.getRoomId(), 0L).intValue(),
			opponent.getPhoto(),
			createdAt
		);
	}
}
