package com.napzak.domain.push.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import com.napzak.domain.push.crud.PushDeviceTokenRemover;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmPushSender {

	private final FirebaseMessaging firebaseMessaging;
	private final PushDeviceTokenRemover pushDeviceTokenRemover;

	public void sendMessage(Long storeId, String deviceToken, String title, String body, Map<String, String> data) {
		try {
			Message message = Message.builder()
				.setToken(deviceToken)
				.setNotification(Notification.builder()
					.setTitle(title)
					.setBody(body)
					.build())
				.putAllData(data)
				.build();

			firebaseMessaging.send(message);
			log.info("푸시 메시지가 보내졌음." + message);
		} catch (FirebaseMessagingException e) {
			if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
				// DB에서 무효 토큰 삭제
				pushDeviceTokenRemover.delete(storeId, deviceToken);
				log.warn("무효 토큰 삭제됨.");
			} else {
				log.warn("FCM push 실패: ", e);
				log.warn("FCM Push 실패: {}", e.getMessagingErrorCode());
			}
		}
	}
}