package org.frank.moviecatalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    // rest template
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // web client
//    @Bean
//    public WebClient webClientBuilder(WebClient.Builder builder) {
//        return builder.build();
//    }

    // web client builder
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
