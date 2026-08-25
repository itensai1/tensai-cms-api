package com.tensai.cms.telegram.api.commands;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Button(
        @NotNull(message = "required")
        String text,

        @NotNull(message = "required")
        ButtonType type,

        @NotNull(message = "required")
        String value
) {
}