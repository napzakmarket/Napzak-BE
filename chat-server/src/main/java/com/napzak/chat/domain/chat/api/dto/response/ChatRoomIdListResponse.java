package com.napzak.chat.domain.chat.api.dto.response;

import java.util.List;

public record ChatRoomIdListResponse(
	List<Long> chatRoomIds
) {
	public static ChatRoomIdListResponse of(List<Long> chatRoomIds) {
		return new ChatRoomIdListResponse(chatRoomIds);
	}
}
