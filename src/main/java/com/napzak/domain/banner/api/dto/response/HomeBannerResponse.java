package com.napzak.domain.banner.api.dto.response;

public record HomeBannerResponse(
	Long bannerId,
	String bannerPhoto,
	String bannerUrl,
	int bannerSequence,
	Boolean isExternal
) {
	public static HomeBannerResponse of(
		final Long bannerId,
		final String bannerPhoto,
		final String bannerUrl,
		final int bannerSequence,
		final Boolean isExternal
	) {
		return new HomeBannerResponse(
			bannerId,
			bannerPhoto,
			bannerUrl,
			bannerSequence,
			isExternal
		);
	}
}