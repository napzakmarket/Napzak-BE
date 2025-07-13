package com.napzak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

	@Value("${firebase.config-path}")
	private String firebaseConfigPath;

	@Bean
	public FirebaseMessaging firebaseMessaging() {
		try (InputStream serviceAccount = getClass().getClassLoader()
			.getResourceAsStream(firebaseConfigPath.replace("classpath:", ""))) {
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			FirebaseApp app;
			if (FirebaseApp.getApps().isEmpty()) {
				app = FirebaseApp.initializeApp(options);
			} else {
				app = FirebaseApp.getInstance();
			}

			return FirebaseMessaging.getInstance(app);
		} catch (Exception e) {
			throw new RuntimeException("FirebaseMessaging 초기화 실패", e);
		}
	}
}
