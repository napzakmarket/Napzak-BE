package com.napzak.chat.domain.chat.api.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ChatRoomListResponse(
	List<ChatRoomSummary> chatRooms,
	Long currentStoreId,

 	@JsonInclude(JsonInclude.Include.NON_NULL)
	Boolean isMessageAllowed
) {
	public static ChatRoomListResponse of(List<ChatRoomSummary> summaries, Long currentStoreId, Boolean isMessageAllowed) {
		// createdAt 최신순으로 정렬
		List<ChatRoomSummary> sorted = summaries.stream()
			.sorted((a, b) -> {
				return b.createdAt().compareTo(a.createdAt());
			})
			.toList();

		return new ChatRoomListResponse(sorted, currentStoreId, isMessageAllowed);
	}
}
