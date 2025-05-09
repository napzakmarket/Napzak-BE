package com.napzak.domain.external.core;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TermsBundleRetriever {

	private final TermsBundleRepository termsBundleRepository;

	public int getActiveBundleVersion(){
		return termsBundleRepository.findByIsActive(true).getVersion();
	}
}
