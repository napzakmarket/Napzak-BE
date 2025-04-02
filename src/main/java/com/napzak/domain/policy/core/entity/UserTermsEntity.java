package com.napzak.domain.policy.core.entity;

import static com.napzak.domain.policy.core.entity.UserTermsTableConstants.*;

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

@Table(name = TABLE_USER_TERMS)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_TERMS_TITLE)
	private String termsTitle;

	@Column(name = COLUMN_TERMS_URL)
	private String termsUrl;

	@Builder
	public UserTermsEntity(String termsTitle, String termsUrl) {
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
	}

	public static UserTermsEntity create(String termsTitle, String termsUrl) {
		return new UserTermsEntity(termsTitle, termsUrl);
	}
}
