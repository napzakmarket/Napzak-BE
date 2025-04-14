package com.napzak.domain.store.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.LinkRetriever;
import com.napzak.domain.external.core.entity.enums.LinkType;
import com.napzak.domain.external.core.vo.Link;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreLinkFacade {

	private final LinkRetriever linkRetriever;

	public Link findByLinkType(LinkType linkType) {
		return linkRetriever.retrieveLinkByLinkType(linkType);
	}
}
