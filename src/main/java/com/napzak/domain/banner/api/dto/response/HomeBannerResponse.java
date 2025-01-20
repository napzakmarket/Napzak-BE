package com.napzak.domain.banner.api.dto.response;

public record HomeBannerResponse(
	Long bannerId,
	String bannerPhoto,
	String bannerUrl,
	int bannerSequence
) {
	public static HomeBannerResponse of(
		final Long bannerId,
		final String bannerPhoto,
		final String bannerUrl,
		final int bannerSequence
	) {
		return new HomeBannerResponse(
			bannerId,
			bannerPhoto,
			bannerUrl,
			bannerSequence
		);
	}
}