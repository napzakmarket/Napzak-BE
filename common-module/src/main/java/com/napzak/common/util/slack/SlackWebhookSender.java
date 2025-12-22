package com.napzak.common.util.slack;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackWebhookSender {

	private final SlackWebhookProperties webhookProperties;
	private final RestTemplate restTemplate;

	public void send(String webhookUrl, String text) {
		try {
			Map<String, Object> body = Map.of("text", text);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			restTemplate.postForEntity(webhookUrl, new HttpEntity<>(body, headers), String.class);
		} catch (Exception e) {
			log.warn("Failed to send slack webhook", e);
		}
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
