package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;
import com.tensai.cms.workspace.internal.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ClearSummaryBotCommand implements BotCommand {
    private final ApplicationEventPublisher publisher;
    private final DraftService draftService;

    @Override
    public String getCommand() {
        return "/clearsummary";
    }

    @Override
    public String getDescription() {
        return "Clear the summary of this blog";
    }

    @Override
    public String getUsage() {
        return "send " + getCommand() + " to clear summary";
    }

    @Override
    public ChatType getChatType() {
        return ChatType.TOPIC;
    }


    @Override
    @Transactional
    public void handle(CreateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        draftService.deleteDraftSummary(event.chatId(), event.messageThreadId());
        message(event.chatId(), event.messageThreadId(), event.messageId(),
                "Successfully cleared the summary");
    }

    @Override
    @Transactional
    public void handle(UpdateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.messageId());
        draftService.deleteDraftSummary(event.chatId(), event.messageThreadId());
        message(event.chatId(), event.messageThreadId(), event.messageId(),
                "Successfully cleared the summary");
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
