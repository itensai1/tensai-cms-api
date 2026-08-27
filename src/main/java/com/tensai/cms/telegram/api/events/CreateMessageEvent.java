package com.tensai.cms.telegram.api.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreateMessageEvent(

        @JsonProperty("chat_id")
        Long chatId,

        @JsonProperty("message_thread_id")
        Long messageThreadId,

        @JsonProperty("message_id")
        Integer messageId,

        Integer date,

        String text,

        String caption,

        TelegramFile video,

        TelegramFile audio,

        TelegramFile document,

        List<TelegramFile> photo,

        List<MessageEntity> entities,

        @JsonProperty("reply_to_message")
        CreateMessageEvent replyToMessage


) {
}
