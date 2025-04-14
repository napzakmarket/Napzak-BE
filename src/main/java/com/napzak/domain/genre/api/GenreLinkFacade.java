package com.napzak.domain.genre.api;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.LinkRetriever;
import com.napzak.domain.external.core.entity.enums.LinkType;
import com.napzak.domain.external.core.vo.Link;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreLinkFacade {

	private final LinkRetriever linkRetriever;

	public Link findByLinkType(LinkType linkType) {
		return linkRetriever.retrieveLinkByLinkType(linkType);
	}
}
