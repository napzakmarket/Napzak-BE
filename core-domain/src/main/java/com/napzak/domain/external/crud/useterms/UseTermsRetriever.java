package com.napzak.domain.external.crud.useterms;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.entity.UseTermsEntity;
import com.napzak.domain.external.entity.enums.TermsType;
import com.napzak.domain.external.repository.UseTermsRepository;
import com.napzak.domain.external.vo.UseTerms;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UseTermsRetriever {
	private final UseTermsRepository useTermsRepository;

	public UseTerms retrieveUseTermsByTermsType(TermsType termsType, int bundleId) {
		UseTermsEntity useTermsEntity = useTermsRepository.findByTermsTitleAndBundleId(termsType, bundleId);
		return UseTerms.fromEntity(useTermsEntity);
	}
}
