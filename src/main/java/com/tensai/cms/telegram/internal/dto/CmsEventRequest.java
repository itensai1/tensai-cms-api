package com.tensai.cms.telegram.internal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tensai.cms.telegram.api.events.*;

public record CmsEventRequest(

        @JsonProperty("event_type")
        EventType eventType,

        @JsonProperty("register_user")
        RegisterUserEvent registerUser,

        @JsonProperty("create_topic")
        CreateTopicEvent createTopic,

        @JsonProperty("update_topic")
        UpdateTopicEvent updateTopic,

        @JsonProperty("create_message")
        CreateMessageEvent createMessage,

        @JsonProperty("update_message")
        UpdateMessageEvent updateMessage,

        @JsonProperty("callback_query")
        CallbackQueryEvent callbackQuery
) {
}
