package com.tensai.cms.storage.internal.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class StorageConfig {
    private final StorageProperties properties;

    @Bean
    S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(properties.accessKey(), properties.secretKey());

        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(StaticCredentialsProvider.create(credentials));

        // If endpoint is provided, we are using MinIO. Path style access is required.
        if (properties.endpoint() != null && !properties.endpoint().isEmpty()) {
            builder.endpointOverride(URI.create(properties.endpoint()))
                    .forcePathStyle(true);
        }

        return builder.build();
    }
}
