package com.tensai.cms.telegram.api.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteMessageCommand(

        @NotNull(message = "required")
        @JsonProperty("chat_id")
        Long chatId,

        @NotNull(message = "required")
        @JsonProperty("message_id")
        Integer messageId
) {
}
