package com.napzak.domain.external.crud.termsbundle;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.repository.TermsBundleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TermsBundleRetriever {

	private final TermsBundleRepository termsBundleRepository;

	public int getActiveBundleVersion(){
		return termsBundleRepository.findByIsActive(true).getVersion();
	}
}
