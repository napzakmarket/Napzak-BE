package com.napzak.domain.store.api.dto.response;

import java.util.List;

public record OnboardingTermsListResponse(
	List<TermsDto> termList
) {
	public static OnboardingTermsListResponse from(List<TermsDto> termsList) {
		return new OnboardingTermsListResponse(termsList);
	}
}
