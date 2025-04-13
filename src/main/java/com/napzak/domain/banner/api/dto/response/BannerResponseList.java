package com.napzak.domain.banner.api.dto.response;

import java.util.List;

public record BannerResponseList<H>(
	List<HomeBannerResponse> TopBannerList,
	List<HomeBannerResponse> MiddleBannerList,
	List<HomeBannerResponse> BottomBannerList
) {
	public static BannerResponseList of(
		List<HomeBannerResponse> topBannerList,
		List<HomeBannerResponse> middleBannerList,
		List<HomeBannerResponse> bottomBannerList
	) {
		return new BannerResponseList(topBannerList, middleBannerList, bottomBannerList);
	}
}
