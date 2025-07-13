package com.napzak.domain.external.crud.link;

import org.springframework.stereotype.Component;

import com.napzak.domain.external.entity.LinkEntity;
import com.napzak.domain.external.entity.enums.LinkType;
import com.napzak.domain.external.repository.LinkRepository;
import com.napzak.domain.external.vo.Link;

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
