package com.tensai.cms.telegram.internal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramProperties(
        String header,
        String secret,
        String url,
        Endpoints endpoints
) {
    public record Endpoints(
            String postCommand,
            String getFile
    ){}
}
