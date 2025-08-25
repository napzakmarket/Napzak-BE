package com.napzak.common.util.discord;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiscordWebhookSender {

	private final DiscordWebhookProperties webhookProperties;
	private final RestTemplate restTemplate = new RestTemplate();

	public void send(String webhookUrl, String content) {
		Map<String, String> body = Map.of("content", content);
		restTemplate.postForEntity(webhookUrl, body, String.class);
	}

	public void sendWithdraw(String message) {
		send(webhookProperties.withdraw(), message);
	}

	public void sendStoreReport(String message) {
		send(webhookProperties.storeReport(), message);
	}

	public void sendProductReport(String message) {
		send(webhookProperties.productReport(), message);
	}
}
