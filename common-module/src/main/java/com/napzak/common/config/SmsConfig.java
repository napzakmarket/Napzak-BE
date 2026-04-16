package com.napzak.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.nurigo.sdk.message.service.DefaultMessageService;

import com.napzak.common.util.sms.SmsProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SmsConfig {

	private final SmsProperties smsProperties;

	@Bean
	public DefaultMessageService messageService() {

		String apiKey = smsProperties.getCoolsms().getApiKey();
		String apiSecret = smsProperties.getCoolsms().getApiSecret();
		return new DefaultMessageService(apiKey, apiSecret, "https://api.coolsms.co.kr");
	}
}
