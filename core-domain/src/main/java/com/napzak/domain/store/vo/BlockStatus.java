package com.napzak.domain.store.vo;

public record BlockStatus(
	boolean isOpponentStoreBlocked, // 상대방 상점을 차단했는지 여부
	boolean isChatBlocked // 상대방이 내 상점을 차단했는지 여부
) {
}
