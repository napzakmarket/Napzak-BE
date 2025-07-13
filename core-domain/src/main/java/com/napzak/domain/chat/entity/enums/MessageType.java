package com.napzak.domain.chat.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
	TEXT,
	IMAGE,
	PRODUCT,
	SYSTEM,
	DATE
}
