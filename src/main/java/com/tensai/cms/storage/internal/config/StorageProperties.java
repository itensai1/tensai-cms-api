package com.tensai.cms.storage.internal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage.s3")
public record StorageProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String region,
        String bucket
) {
}
