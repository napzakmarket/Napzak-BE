package com.napzak.api.domain.store;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.crud.termsbundle.TermsBundleRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreTermsBundleFacade {

	private final TermsBundleRetriever termsBundleRetriever;

	public int findActiveBundleId() {
		return termsBundleRetriever.getActiveBundleVersion();
	}
}
