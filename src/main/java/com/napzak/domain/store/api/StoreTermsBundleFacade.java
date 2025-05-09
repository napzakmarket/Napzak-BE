package com.napzak.domain.store.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.TermsBundleRetriever;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreTermsBundleFacade {

	private final TermsBundleRetriever termsBundleRetriever;

	public int findActiveBundleId() {
		return termsBundleRetriever.getActiveBundleVersion();
	}
}
