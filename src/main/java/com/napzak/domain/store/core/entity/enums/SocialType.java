package com.napzak.domain.store.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
	KAKAO("KAKAO"),
	APPLE("APPLE"),
	GOOGLE("GOOGLE");

	private final String type;
}
