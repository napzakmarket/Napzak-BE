package com.napzak.common.util.slack;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackWebhookSender {

	private final SlackWebhookProperties webhookProperties;
	private final RestTemplate restTemplate;

	public void send(String webhookUrl, String text) {
		if (webhookUrl == null || webhookUrl.isBlank()) {
			log.error("Slack webhook url is empty. Skip sending message.");
			return;
		}

		Map<String, Object> body = Map.of("text", text);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(
				webhookUrl,
				new HttpEntity<>(body, headers),
				String.class
			);

			if (!response.getStatusCode().is2xxSuccessful()) {
				log.error("Failed to send slack webhook. status={}, body={}",
					response.getStatusCode(), response.getBody());
			}
		} catch (RestClientException exception) {
			log.error("Failed to send slack webhook. message={}", exception.getMessage(), exception);
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
