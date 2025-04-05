package com.napzak.domain.policy.core.entity;

import static com.napzak.domain.policy.core.entity.LinkTableConstants.*;

import com.napzak.domain.policy.core.entity.enums.LinkType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_LINK)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LinkEntity {

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
	public LinkEntity(String url, LinkType type) {
		this.url = url;
		this.type = type;
	}

	public static LinkEntity create(String url, LinkType type) {
		return LinkEntity.builder()
			.url(url)
			.type(type)
			.build();
	}
}