package com.tensai.cms.auth.internal.web.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "required")
        String username,

        @NotNull(message = "required")
        String password
) {
}
