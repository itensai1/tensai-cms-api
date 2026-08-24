package com.tensai.cms.telegram.api.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CallbackQueryEvent(
        @JsonProperty("chat_id")
        Long chatId,

        @JsonProperty("message_thread_id")
        Integer messageThreadId,

        @JsonProperty("message_id")
        Integer messageId,

        @JsonProperty("callback_query_id")
        String callbackQueryId,

        String data
) {
}
