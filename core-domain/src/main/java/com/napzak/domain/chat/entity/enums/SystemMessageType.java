package com.napzak.domain.chat.entity.enums;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemMessageType {
	EXIT("EXIT", "상대방이 채팅방을 나갔습니다."),
	WITHDRAWN("WITHDRAWN", "상대방과 대화가 불가능합니다."),
	REPORTED("REPORTED", "현재 이용이 제한된 유저입니다.");

	private final String type;
	private final String content;

	public Map<String, Object> getMetadataMap() {
		return Map.of(
			"type", this.type,
			"content", this.content
		);
	}
}
