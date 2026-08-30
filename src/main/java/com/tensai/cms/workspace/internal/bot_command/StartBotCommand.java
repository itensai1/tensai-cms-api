package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartBotCommand implements BotCommand {
    private final ApplicationEventPublisher publisher;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "start private chat";
    }

    @Override
    public String getUsage() {
        return "send " + getCommand() + " in private chat";
    }

    @Override
    public ChatType getChatType() {
        return ChatType.PRIVATE;
    }


    @Override
    public void handle(CreateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        message(event.chatId(), null, null,
                """
                        Welcome to Tensai Content Management System!
                        
                        > to manage your Blog send /settings
                        
                        > to know how to create Blog visit
                            - url: link-to-tutorial
                        
                        """);

        // TODO: add /settings and tutorial page link
    }

    @Override
    public void message(Long chatId, Long messageThreadId, Integer messageId, String message) {
        publisher.publishEvent(
                SendMessageCommand.builder()
                        .chatId(chatId)
                        .text(message)
                        .replyToMessageId(messageId)
                        .messageThreadId(messageThreadId)
                        .build());
    }
}
