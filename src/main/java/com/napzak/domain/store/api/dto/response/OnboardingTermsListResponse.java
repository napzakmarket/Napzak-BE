package com.napzak.domain.store.api.dto.response;

import java.util.List;

public record OnboardingTermsListResponse(
	int bundleId,
	List<TermsDto> termList
) {
	public static OnboardingTermsListResponse from(int bundleId, List<TermsDto> termsList) {
		return new OnboardingTermsListResponse(bundleId, termsList);
	}
}
