package com.napzak.common.swagger.config;

import java.util.Collections;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.napzak.common.swagger.annotation.DisableSwaggerSecurity;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Value("${app.server.url}")
	private String serverUrl;

	@Bean
	public OpenAPI openAPI() {
		String jwt = "JWT";
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

		Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
			.name(jwt)
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
		);

		return new OpenAPI()
			.addServersItem(new Server().url(serverUrl))
			.components(components)
			.info(apiInfo())
			.addSecurityItem(securityRequirement);
	}

	@Bean
	public GroupedOpenApi generalApi() {
		return GroupedOpenApi.builder()
			.group("general")
			.pathsToMatch("/**")
			.pathsToExclude("/api/admin/**")
			.addOperationCustomizer(customize())
			.build();
	}

	@Bean
	public GroupedOpenApi adminApi() {
		return GroupedOpenApi.builder()
			.group("admin")
			.pathsToMatch("/api/admin/**")
			.addOperationCustomizer(customize())
			.build();
	}

	@Bean
	public OperationCustomizer customize() {
		return (operation, handlerMethod) -> {
			DisableSwaggerSecurity methodAnnotation = handlerMethod.getMethodAnnotation(DisableSwaggerSecurity.class);
			if (methodAnnotation != null) {
				operation.setSecurity(Collections.emptyList());
			}
			return operation;
		};
	}

	private Info apiInfo() {
		return new Info()
			.title("NapzakMarket Project API")
			.description("덕후를 위한 서브컬쳐 중고거래 플랫폼")
			.version("1.0.0");
	}
}
