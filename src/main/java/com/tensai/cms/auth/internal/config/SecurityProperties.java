package com.tensai.cms.auth.internal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(

        List<String> allowedOrigins,

        String telegramHeader,

        String jwtSecret,

        String baseUrl,

        String resetPasswordPath
) {
}
