package com.napzak.api.domain.store;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.crud.link.LinkRetriever;
import com.napzak.domain.external.entity.enums.LinkType;
import com.napzak.domain.external.vo.Link;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StoreLinkFacade {

	private final LinkRetriever linkRetriever;

	public Link findByLinkType(LinkType linkType) {
		return linkRetriever.retrieveLinkByLinkType(linkType);
	}
}
