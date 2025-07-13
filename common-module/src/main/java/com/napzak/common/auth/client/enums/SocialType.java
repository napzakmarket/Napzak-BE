package com.napzak.common.auth.client.enums;

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
