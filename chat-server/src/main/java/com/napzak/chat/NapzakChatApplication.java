package com.napzak.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
	"com.napzak",
	"websocket.pubsub"
})
@EnableScheduling
@EntityScan(basePackages = "com.napzak.domain")
@EnableJpaRepositories(basePackages = "com.napzak.domain")
public class NapzakChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(NapzakChatApplication.class, args);
	}

}
