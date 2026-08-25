package com.tensai.cms.telegram.internal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.tensai.cms.telegram.api.commands.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CmsCommand(

        @NotNull(message = "required")
        @JsonProperty("command_type")
        CommandType commandType,

        @JsonProperty("send_message")
        SendMessageCommand sendMessage,

        @JsonProperty("delete_message")
        DeleteMessageCommand deleteMessage,

        @JsonProperty("delete_topic")
        DeleteTopicCommand deleteTopic,

        @JsonProperty("edit_keyboard")
        EditKeyboardCommand editKeyboard,

        @JsonProperty("answer_callback")
        AnswerCallbackCommand answerCallback
) {
}
