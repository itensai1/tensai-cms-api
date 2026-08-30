package com.tensai.cms.workspace.internal.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BlogInfo(
        UUID id,
        String title,
        String summary,
        @JsonProperty("last_updated")
        Instant lastUpdated,
        @JsonProperty("user_id")
        UUID userId,
        String username,
        @JsonProperty("publisher_name")
        String publisherName
) {
}
