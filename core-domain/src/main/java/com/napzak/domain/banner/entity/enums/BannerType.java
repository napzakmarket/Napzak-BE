package com.napzak.domain.banner.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BannerType {

	TOP("TOP_BANNER"),
	MIDDLE("MIDDLE_BANNER"),
	BOTTOM("BOTTOM_BANNER"),
	;

	public final String bannerType;

	private String getBannerType() {
		return bannerType;
	}

}
