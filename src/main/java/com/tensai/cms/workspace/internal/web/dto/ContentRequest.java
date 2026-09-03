package com.tensai.cms.workspace.internal.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ContentRequest(
        @NotBlank(message = "required")
        String content
) {
}
