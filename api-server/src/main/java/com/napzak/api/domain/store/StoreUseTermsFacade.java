package com.napzak.api.domain.store;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.crud.useterms.UseTermsRetriever;
import com.napzak.domain.external.entity.enums.TermsType;
import com.napzak.domain.external.vo.UseTerms;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreUseTermsFacade {

	private final UseTermsRetriever useTermsRetriever;

	public UseTerms findByTermsTypeAndBundleId(TermsType termsType, int bundleId) {
		return useTermsRetriever.retrieveUseTermsByTermsType(termsType, bundleId);
	}
}
