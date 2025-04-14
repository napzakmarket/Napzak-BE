package com.napzak.domain.store.api.dto.response;

public record TermsDto(
	Long termsId,
	String termsTitle,
	String termsUrl
) {
	public static TermsDto from(
		Long termsId,
		String termsTitle,
		String termsUrl
	) {
		return new TermsDto(termsId, termsTitle, termsUrl);
	}
}
