package com.napzak.domain.external.core.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TermsType {
	TERMS("이용약관"),
	PRIVACY_POLICY("개인정보 처리방침"),
	;

	private final String linkType;
}
