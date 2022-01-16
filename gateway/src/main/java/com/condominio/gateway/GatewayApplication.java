package com.condominio.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/entrar/**", "/login/**")
						.uri("http://localhost:8081"))
				.route(p -> p
						.path("/sindico/contas/**")
						.uri("http://localhost:8082"))
				.route(p -> p
						.path("/conta/redefinir")
						.uri("http://localhost:8083"))
				.route(p -> p
						.path("/**")
						.uri("http://localhost:8081"))
				.route(p -> p
						.path("/js/**","/css/**","/imagens/**","/webfonts/**")
						.uri("http://localhost:8081"))
				.build();
	}
}
