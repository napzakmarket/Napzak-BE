package com.napzak.api.domain.genre;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.crud.link.LinkRetriever;
import com.napzak.domain.external.entity.enums.LinkType;
import com.napzak.domain.external.vo.Link;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreLinkFacade {

	private final LinkRetriever linkRetriever;

	public Link findByLinkType(LinkType linkType) {
		return linkRetriever.retrieveLinkByLinkType(linkType);
	}
}
