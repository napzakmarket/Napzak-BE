package com.napzak.domain.external.core;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.core.entity.LinkEntity;
import com.napzak.domain.external.core.entity.enums.LinkType;
import com.napzak.domain.external.core.vo.Link;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LinkRetriever {

	private final LinkRepository linkRepository;

	public Link retrieveLinkByLinkType(LinkType linkType){
		LinkEntity linkEntity = linkRepository.findByType(linkType);
		return Link.fromEntity(linkEntity);
	}
}
