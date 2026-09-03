package com.tensai.cms.workspace.internal.repository.projection;

import java.time.Instant;
import java.util.UUID;

public interface BlogProjection {
    UUID getId();

    String getTitle();

    String getSummary();

    Instant getUpdatedAt();

    UUID getUserId();

    int getLikesCount();

    int getCommentsCount();
}
