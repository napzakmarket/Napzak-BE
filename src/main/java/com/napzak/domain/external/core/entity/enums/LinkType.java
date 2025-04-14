package com.napzak.domain.external.core.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LinkType {
	NOTICE("공지사항"),             // 공지사항
	VERSION_INFO("버전 정보"),       // 버전 정보
	GENRE_REQUEST("장르 추가 요청"),      // 장르 추가 요청
	CUSTOMER_SUPPORT("고객센터"),
	TERMS("이용약관"),
	PRIVACY_POLICY("개인정보 처리방침");

	public final String linkType;
}
