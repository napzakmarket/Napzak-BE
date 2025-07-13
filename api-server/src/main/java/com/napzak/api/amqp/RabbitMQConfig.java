package com.napzak.api.amqp;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "chat.exchange";
	public static final String MAIN_QUEUE = "chat.system.queue";
	public static final String DLQ_QUEUE = "chat.system.dlq";
	public static final String RETRY_QUEUE = "chat.system.retry";
	public static final String ROUTING_KEY = "chat.system";
	public static final String RETRY_ROUTING_KEY = "chat.system.retry";

	// 1️⃣ Exchange
	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	// 2️⃣ 메인 Queue
	@Bean
	public Queue mainQueue() {
		return QueueBuilder.durable(MAIN_QUEUE)
			.withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
			.withArgument("x-dead-letter-routing-key", RETRY_ROUTING_KEY)
			.build();
	}

	// 3️⃣ Retry Queue (Delay 후 메인으로)
	@Bean
	public Queue retryQueue() {
		return QueueBuilder.durable(RETRY_QUEUE)
			.withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
			.withArgument("x-dead-letter-routing-key", ROUTING_KEY)
			.withArgument("x-message-ttl", 5000) // 5초 후 Main 으로 복귀
			.build();
	}

	// 4️⃣ DLQ (최종 실패 저장)
	@Bean
	public Queue dlqQueue() {
		return QueueBuilder.durable(DLQ_QUEUE).build();
	}

	// 5️⃣ Binding
	@Bean
	public Binding mainBinding() {
		return BindingBuilder.bind(mainQueue()).to(chatExchange()).with(ROUTING_KEY);
	}

	@Bean
	public Binding retryBinding() {
		return BindingBuilder.bind(retryQueue()).to(chatExchange()).with(RETRY_ROUTING_KEY);
	}

	@Bean
	public Binding dlqBinding() {
		return BindingBuilder.bind(dlqQueue()).to(chatExchange()).with(DLQ_QUEUE);
	}

	// 6️⃣ Template + Retry
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(new Jackson2JsonMessageConverter());
		template.setRetryTemplate(retryTemplate());
		return template;
	}

	@Bean
	public RetryTemplate retryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
		FixedBackOffPolicy backOff = new FixedBackOffPolicy();
		backOff.setBackOffPeriod(2000);
		retryTemplate.setBackOffPolicy(backOff);
		return retryTemplate;
	}
}
