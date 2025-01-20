package com.napzak.domain.banner.api.dto.response;

import java.util.List;

public record BannerResponseList<H>(
	List<HomeBannerResponse> bannerList
) {
	public static BannerResponseList of(
		List<HomeBannerResponse> bannerList
	) {
		return new BannerResponseList(bannerList);
	}
}
