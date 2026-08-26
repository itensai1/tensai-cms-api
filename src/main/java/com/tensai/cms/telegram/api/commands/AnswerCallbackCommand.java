package com.tensai.cms.telegram.api.commands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record AnswerCallbackCommand(

        @NotNull(message = "required")
        @JsonProperty("callback_query_id")
        String callbackQueryId,

        String text
) {
}
