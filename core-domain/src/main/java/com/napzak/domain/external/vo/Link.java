package com.napzak.domain.external.vo;

import com.napzak.domain.external.entity.LinkEntity;
import com.napzak.domain.external.entity.enums.LinkType;

import lombok.Getter;

@Getter
public class Link {
	private final Long id;
	private final String url;
	private final LinkType type;

	public Link(Long id, String url, LinkType type) {
		this.id = id;
		this.url = url;
		this.type = type;
	}

	public static Link fromEntity(LinkEntity linkEntity) {
		return new Link(
			linkEntity.getId(),
			linkEntity.getUrl(),
			linkEntity.getType()
		);
	}
}