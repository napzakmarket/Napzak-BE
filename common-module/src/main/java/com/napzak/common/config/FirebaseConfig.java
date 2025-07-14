package com.napzak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

	@Value("${firebase.config-path}")
	private String firebaseConfigPath;

	@Bean
	public FirebaseMessaging firebaseMessaging() {
		try (InputStream serviceAccount = getInputStream(firebaseConfigPath)) {
			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			FirebaseApp app = FirebaseApp.getApps().isEmpty()
				? FirebaseApp.initializeApp(options)
				: FirebaseApp.getInstance();

			return FirebaseMessaging.getInstance(app);
		} catch (Exception e) {
			throw new RuntimeException("FirebaseMessaging 초기화 실패", e);
		}
	}

	private InputStream getInputStream(String path) throws Exception {
		if (path.startsWith("classpath:")) {
			String resourcePath = path.replace("classpath:", "");
			InputStream resource = getClass().getClassLoader().getResourceAsStream(resourcePath);
			if (resource == null) throw new RuntimeException("Firebase 파일을 classpath에서 찾을 수 없습니다: " + resourcePath);
			return resource;
		} else if (path.startsWith("file:")) {
			String filePath = path.replace("file:", "");
			return new FileInputStream(filePath);
		}
		throw new IllegalArgumentException("지원하지 않는 firebase.config-path 형식: " + path);
	}
}
