package com.napzak.domain.external.core.entity;

import static com.napzak.domain.external.core.entity.UseTermsTableConstants.*;

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

@Table(name = TABLE_USE_TERMS)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UseTermsEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_TERMS_TITLE)
	private String termsTitle;

	@Column(name = COLUMN_TERMS_URL)
	private String termsUrl;

	@Builder
	public UseTermsEntity(String termsTitle, String termsUrl) {
		this.termsTitle = termsTitle;
		this.termsUrl = termsUrl;
	}

	public static UseTermsEntity create(String termsTitle, String termsUrl) {
		return new UseTermsEntity(termsTitle, termsUrl);
	}
}
