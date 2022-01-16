package com.condominio.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebfluxSecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange().anyExchange().permitAll().and().csrf().disable();
//                .pathMatchers("/entrar", "/login").permitAll()
//                .pathMatchers("/js/**","/css/**","/imagens/**","/webfonts/**").permitAll()
//                .anyExchange().authenticated();

        return http.build();
    }
}
