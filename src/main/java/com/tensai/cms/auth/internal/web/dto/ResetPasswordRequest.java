package com.tensai.cms.auth.internal.web.dto;

import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
        @NotNull(message = "required")
        String password
) {
}
