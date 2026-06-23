package com.cine.loyalty_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${userservice.url:http://localhost:8081}")
    private String userServiceUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

}
