package com.napzak.domain.member.core;

import static com.napzak.domain.member.core.MemberTableConstants.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
	name = TABLE_MEMBER,
	indexes = {
		@Index(name = "uk1", columnList = "phone", unique = true)
	}
)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {

	@Id
	@Column(name = COLUMN_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = COLUMN_NICKNAME, nullable = false)
	private String nickname;

	@Column(name = COLUMN_PHONE, nullable = false)
	private String phone;

	@Column(name = COLUMN_PHOTO, nullable = true)
	private String photo;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_ROLE, nullable = false, columnDefinition = "varchar(10) default 'MEMBER'")
	private Role role;

	@Column(name = COLUMN_CREATED_AT, nullable = false)
	private final LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = COLUMN_DELETED_AT, nullable = true)
	private LocalDateTime deletedAt;

	@Column(name = COLUMN_SOCIAL_ID, nullable = true)
	private Long socialId;

	@Enumerated(EnumType.STRING)
	@Column(name = COLUMN_SOCIAL_TYPE, nullable = true)
	private SocialType socialType;

	@Builder
	private MemberEntity(String nickname, String phone, String photo, Role role, Long socialId, SocialType socialType) {
		this.nickname = nickname;
		this.phone = phone;
		this.photo = photo;
		this.role = role;
		this.socialId = socialId;
		this.socialType = socialType;
	}

	public static MemberEntity create(
		final String nickname,
		final String phone,
		final String photo,
		final Role role,
		final Long socialId,
		final SocialType socialType
	) {
		return MemberEntity.builder()
			.nickname(nickname)
			.phone(phone)
			.photo(photo)
			.role(role)
			.socialId(socialId)
			.socialType(socialType)
			.build();
	}
}
