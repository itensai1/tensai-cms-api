package com.tensai.cms.telegram.internal.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final TelegramProperties properties;

    @Bean
    public RestClient telegramGatewayClient() {
        return RestClient.builder()
                .baseUrl(properties.url())
                .defaultHeader(properties.header(), properties.secret())
                .build();
    }

}
