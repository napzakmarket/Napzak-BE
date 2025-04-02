package com.napzak.domain.policy.core.entity;

import static com.napzak.domain.policy.core.entity.LinkResourceTableConstants.*;

import com.napzak.domain.policy.core.entity.enums.LinkType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_LINK_RESOURCE)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkResourceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_URL, nullable = false)
	private String url;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_TYPE, nullable = false)
	private LinkType type;

	@Builder
	public LinkResourceEntity(String url, LinkType type) {
		this.url = url;
		this.type = type;
	}

	public static LinkResourceEntity create(String url, LinkType type) {
		return LinkResourceEntity.builder()
			.url(url)
			.type(type)
			.build();
	}
}