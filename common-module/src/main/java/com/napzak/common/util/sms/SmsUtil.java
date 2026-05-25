package com.napzak.common.util.sms;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SmsUtil {

	private static final SecureRandom secureRandom = new SecureRandom();
	private final SmsProperties smsProperties;

	public static String generateVerificationCode() {
		int code = 100000 + secureRandom.nextInt(900000);
		return String.valueOf(code);
	}

	public String formatMessageText(String verificationCode) {
		String baseText = smsProperties.getText();
		return String.format(baseText, verificationCode);
	}
}
