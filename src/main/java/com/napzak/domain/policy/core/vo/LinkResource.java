package com.napzak.domain.policy.core.vo;

import com.napzak.domain.policy.core.entity.LinkResourceEntity;
import com.napzak.domain.policy.core.entity.enums.LinkType;

import lombok.Getter;

@Getter
public class LinkResource {
	private final Long id;
	private final String url;
	private final LinkType type;

	public LinkResource(Long id, String url, LinkType type) {
		this.id = id;
		this.url = url;
		this.type = type;
	}

	public static LinkResource fromEntity(LinkResourceEntity entity) {
		return new LinkResource(
			entity.getId(),
			entity.getUrl(),
			entity.getType()
		);
	}
}