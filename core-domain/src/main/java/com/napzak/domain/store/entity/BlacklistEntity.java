package com.napzak.domain.store.entity;

import static com.napzak.domain.store.entity.BlacklistTableConstants.*;

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

@Table(name = TABLE_BLACKLIST)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlacklistEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = COLUMN_ID)
	private Long id;

	@Column(name = COLUMN_STORE_ID, nullable = false)
	private Long storeId;

	@Column(name = COLUMN_PHONE_NUMBER_ENC, nullable = true)
	private String phoneNumberEnc;

	@Column(name = COLUMN_PHONE_NUMBER_HASH, nullable = true, unique = true)
	private String phoneNumberHash;

	@Builder
	public BlacklistEntity(Long storeId, String phoneNumberEnc, String phoneNumberHash) {
		this.storeId = storeId;
		this.phoneNumberEnc = phoneNumberEnc;
		this.phoneNumberHash = phoneNumberHash;
	}

	public static BlacklistEntity create(Long storeId, String phoneNumberEnc, String phoneNumberHash) {
		return BlacklistEntity.builder()
			.storeId(storeId)
			.phoneNumberEnc(phoneNumberEnc)
			.phoneNumberHash(phoneNumberHash)
			.build();
	}
}
