package com.tensai.cms.telegram.api.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateTopicEvent(
        @JsonProperty("chat_id")
        Long chatId,

        @JsonProperty("message_thread_id")
        Long messageThreadId,

        @JsonProperty("topic_name")
        String topicName
) {
}
