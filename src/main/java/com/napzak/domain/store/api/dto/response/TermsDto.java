package com.napzak.domain.store.api.dto.response;

public record TermsDto(
	Long termsId,
	String termsTitle,
	String termsUrl,
	int bundleId
) {
	public static TermsDto from(
		Long termsId,
		String termsTitle,
		String termsUrl,
		int bundleId
	) {
		return new TermsDto(termsId, termsTitle, termsUrl, bundleId);
	}
}
