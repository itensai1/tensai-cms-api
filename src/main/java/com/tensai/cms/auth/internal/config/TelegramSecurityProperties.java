package com.tensai.cms.auth.internal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramSecurityProperties(
        String header,
        String secret
) {
}
