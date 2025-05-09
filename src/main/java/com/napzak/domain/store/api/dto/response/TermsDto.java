package com.napzak.domain.store.api.dto.response;

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
