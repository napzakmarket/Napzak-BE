package com.napzak.domain.store.api.dto.response;

public record SettingLinkResponse(
	String noticeLink,
	String termLink,
	String privacyPolicyLink,
	String versionInfoLink
) {
	public static SettingLinkResponse from(
		String noticeLink,
		String termLink,
		String privacyPolicyLink,
		String versionInfoLink
	) {
		return new SettingLinkResponse(noticeLink, termLink, privacyPolicyLink, versionInfoLink);
	}
}

