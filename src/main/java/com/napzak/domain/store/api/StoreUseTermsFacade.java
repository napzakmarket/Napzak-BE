package com.napzak.domain.store.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.UseTermsRetriever;
import com.napzak.domain.external.core.entity.enums.TermsType;
import com.napzak.domain.external.core.vo.UseTerms;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreUseTermsFacade {

	private final UseTermsRetriever useTermsRetriever;

	public UseTerms findByTermsTypeAndBundleId(TermsType termsType, int bundleId) {
		return useTermsRetriever.retrieveUseTermsByTermsType(termsType, bundleId);
	}
}
