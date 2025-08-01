package com.napzak.chat.domain.chat.api.dto.response;

import java.util.Set;

public record ChatRoomEnterResponse(
	Long productId,
	Set<Long> onlineStoreIds
) {
	public static ChatRoomEnterResponse of(Long productId, Set<Long> onlineStoreIds) {
		return new ChatRoomEnterResponse(productId, onlineStoreIds);
	}
}
