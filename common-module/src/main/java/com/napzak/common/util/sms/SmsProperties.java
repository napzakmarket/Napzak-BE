package com.napzak.common.util.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

	private Coolsms coolsms = new Coolsms();
	private Policy policy = new Policy();
	private String text;

	@Getter
	@Setter
	public static class Coolsms {
		private String apiKey;
		private String apiSecret;
		private String senderNumber;
	}

	@Getter
	@Setter
	public static class Policy {
		private long verifyExpireTime;
		private int sendMaxCount;
		private int failMaxCount;
	}
}