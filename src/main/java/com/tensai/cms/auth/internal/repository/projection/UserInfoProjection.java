package com.tensai.cms.auth.internal.repository.projection;

import java.util.UUID;

public interface UserInfoProjection {
    UUID getId();

    String getUsername();

    String getFirstName();

    String getLastName();
}
