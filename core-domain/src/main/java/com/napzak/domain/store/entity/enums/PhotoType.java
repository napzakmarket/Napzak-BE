package com.napzak.domain.store.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PhotoType {
	PROFILE("profile_image"),
	COVER("cover_image"),
	;

	private final String photoType;
}
