package com.tensai.cms.workspace.internal.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CommentDto(
        UUID id,
        @JsonProperty("user_id")
        UUID userId,
        String content,
        @JsonProperty("last_updated")
        Instant lastUpdated,
        @JsonProperty("is_edited")
        boolean isEdited,
        String username,
        @JsonProperty("commenter_name")
        String commenterName,
        @JsonProperty("is_author")
        boolean isAuthor
) {
}
