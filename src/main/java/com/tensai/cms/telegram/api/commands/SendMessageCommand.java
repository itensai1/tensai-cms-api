package com.tensai.cms.telegram.api.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record SendMessageCommand(

        @NotNull(message = "required")
        @JsonProperty("chat_id")
        Long chatId,

        @NotNull(message = "required")
        String text,

        @JsonProperty("reply_to_message_id")
        Integer replyToMessageId,

        @JsonProperty("message_thread_id")
        Integer messageThreadId,

        Keyboard keyboard
) {
}
