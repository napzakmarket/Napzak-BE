package com.napzak.domain.policy.core.entity;

import static com.napzak.domain.policy.core.entity.UserTermTableConstants.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = TABLE_USER_TERM)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_TERM_TITLE)
	private String termTitle;

	@Column(name = COLUMN_TERM_URL)
	private String termUrl;

	@Builder
	public UserTermEntity(String termTitle, String termUrl) {
		this.termTitle = termTitle;
		this.termUrl = termUrl;
	}

	public static UserTermEntity create(String termTitle, String termUrl) {
		return new UserTermEntity(termTitle, termUrl);
	}
}
