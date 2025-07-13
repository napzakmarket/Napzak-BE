package com.napzak.api.domain.store.dto.response;

public record TermsDto(
	Long termsId,
	String termsTitle,
	String termsUrl,
	boolean isRequired
) {
	public static TermsDto from(
		Long termsId,
		String termsTitle,
		String termsUrl,
		boolean isRequired
	) {
		return new TermsDto(termsId, termsTitle, termsUrl, isRequired);
	}
}
