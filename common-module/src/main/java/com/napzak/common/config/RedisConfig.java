package com.napzak.common.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import lombok.RequiredArgsConstructor;
import com.napzak.common.websocket.pubsub.RedisSubscriber;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.port}")
	private int port;

	@Value("${spring.data.redis.password}")
	private String password;

	public static final String CHAT_TOPIC = "chat-presence";

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host, port);
		redisConfiguration.setPassword(password);
		return new LettuceConnectionFactory(redisConfiguration);
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic(CHAT_TOPIC);
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(ObjectProvider<RedisSubscriber> subscriberProvider) {
		RedisSubscriber redisSubscriber = subscriberProvider.getIfAvailable();
		return redisSubscriber == null ? null : new MessageListenerAdapter(redisSubscriber, "onMessage");
	}

	@Bean
	public RedisMessageListenerContainer redisContainer(
		RedisConnectionFactory connectionFactory,
		ObjectProvider<MessageListenerAdapter> adapterProvider,
		ChannelTopic topic
	) {
		MessageListenerAdapter adapter = adapterProvider.getIfAvailable();
		if (adapter == null) return null;

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(adapter, topic);
		return container;
	}
}