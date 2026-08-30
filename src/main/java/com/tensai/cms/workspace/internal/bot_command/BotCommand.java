package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;

public interface BotCommand {
    String getCommand();

    String getDescription();

    String getUsage();

    ChatType getChatType();

    default void handle(CreateMessageEvent event, String text) {
    }

    default void handle(UpdateMessageEvent event, String text) {
    }

    void message(Long chatId, Long messageThreadId, Integer messageId, String message);
}
