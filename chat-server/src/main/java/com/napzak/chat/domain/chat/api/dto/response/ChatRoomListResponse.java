package com.napzak.chat.domain.chat.api.dto.response;

import java.util.List;

public record ChatRoomListResponse(
	List<ChatRoomSummary> chatRooms
) {
	public static ChatRoomListResponse of(List<ChatRoomSummary> summaries) {
		// createdAt 최신순으로 정렬
		List<ChatRoomSummary> sorted = summaries.stream()
			.sorted((a, b) -> {
				return b.createdAt().compareTo(a.createdAt());
			})
			.toList();

		return new ChatRoomListResponse(sorted);
	}
}
