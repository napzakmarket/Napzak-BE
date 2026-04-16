package com.napzak.domain.store.vo;

public record SmsVerificationData(
	String code,
	int failCount
) {

	public static SmsVerificationData of(String code) {
		return new SmsVerificationData(code, 0);
	}

	public SmsVerificationData incrementFailCount() {

		return new SmsVerificationData(this.code, this.failCount + 1);
	}
}
