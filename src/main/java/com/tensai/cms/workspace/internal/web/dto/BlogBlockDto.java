package com.tensai.cms.workspace.internal.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tensai.cms.workspace.internal.entity.BlockType;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BlogBlockDto(
        UUID id,
        int position,
        BlockType type,
        String text,
        @JsonProperty("media_url")
        String mediaUrl,
        @JsonProperty("last_updated")
        Instant lastUpdated
) {
}
