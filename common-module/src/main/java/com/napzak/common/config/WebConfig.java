package com.napzak.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.napzak.common.auth.resolver.CurrentMemberArgumentResolver;
import com.napzak.common.converter.StringToEnumCustomConverterFactory;
import com.napzak.common.util.discord.DiscordWebhookProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DiscordWebhookProperties.class)
public class WebConfig implements WebMvcConfigurer {

	private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

	@Value("${cors.allowed-origins}")
	private String[] allowedOrigins;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(currentMemberArgumentResolver);
	}

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		final StringToEnumCustomConverterFactory converterFactory = new StringToEnumCustomConverterFactory();
		registry.addConverterFactory(converterFactory);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(allowedOrigins)
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true);
	}
}
