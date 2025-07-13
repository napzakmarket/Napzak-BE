package com.napzak.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
	"com.napzak",
	"websocket.pubsub"
})
@EnableFeignClients(basePackages = "com.napzak.common.auth.client")
@EnableScheduling
@EntityScan(basePackages = "com.napzak.domain")
@EnableJpaRepositories(basePackages = "com.napzak.domain")
@EnableRedisRepositories(basePackages = "com.napzak.common.auth.jwt.repository")
public class NapzakApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NapzakApiApplication.class, args);
	}

}
