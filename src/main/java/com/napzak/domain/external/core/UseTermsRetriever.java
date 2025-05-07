package com.napzak.domain.external.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.entity.UseTermsEntity;
import com.napzak.domain.external.core.entity.enums.TermsType;
import com.napzak.domain.external.core.vo.UseTerms;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UseTermsRetriever {
	private final UseTermsRepository useTermsRepository;

	public UseTerms retrieveUseTermsByTermsType(TermsType termsType) {
		UseTermsEntity useTermsEntity = useTermsRepository.findByTermsTitle(termsType);
		return UseTerms.fromEntity(useTermsEntity);
	}
}
