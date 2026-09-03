package com.tensai.cms.auth.api;

import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        String fullName
) {
    public UserInfo(UUID id, String username, String firstname, String lastname) {
        this(id, username, firstname + (lastname == null ? "" : " " + lastname));
    }

}
