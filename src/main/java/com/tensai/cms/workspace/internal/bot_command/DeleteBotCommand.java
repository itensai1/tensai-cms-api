package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.telegram.api.commands.DeleteMessageCommand;
import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;
import com.tensai.cms.workspace.internal.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DeleteBotCommand implements BotCommand {
    private final ApplicationEventPublisher publisher;
    private final DraftService draftService;

    @Override
    public String getCommand() {
        return "/delete";
    }

    @Override
    public String getDescription() {
        return "Delete specified message by replying to it";
    }

    @Override
    public String getUsage() {
        return "reply to the message you want to delete with " + getCommand();
    }

    @Override
    public ChatType getChatType() {
        return ChatType.TOPIC;
    }

    @Override
    @Transactional
    public void handle(CreateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        if (event.replyToMessage() == null) {
            message(event.chatId(), event.messageThreadId(), event.messageId(),
                    "Please reply to the specific message you wish to delete from blog.");
            return;
        }
        draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.replyToMessage().messageId());

        Instant sentTime = Instant.ofEpochSecond(event.replyToMessage().date());

        if (sentTime.isAfter(Instant.now().minus(Duration.ofDays(2)))) {
            publisher.publishEvent(
                    DeleteMessageCommand.builder().chatId(event.chatId())
                            .messageId(event.replyToMessage().messageId()).build()
            );
        } else {
            message(event.chatId(), event.messageThreadId(), event.replyToMessage().messageId(),
                    "You can safely delete this message from the chat.");
        }
    }

    @Override
    @Transactional
    public void handle(UpdateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        if (event.replyToMessage() == null) {
            message(event.chatId(), event.messageThreadId(), event.messageId(),
                    "Please reply to the specific message you wish to delete from blog.");
            return;
        }
        draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.replyToMessage().messageId());
        draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.messageId());

        Instant sentTime = Instant.ofEpochSecond(event.replyToMessage().date());

        if (sentTime.isAfter(Instant.now().minus(Duration.ofDays(2)))) {
            publisher.publishEvent(
                    DeleteMessageCommand.builder().chatId(event.chatId())
                            .messageId(event.replyToMessage().messageId()).build()
            );
        } else {
            message(event.chatId(), event.messageThreadId(), event.replyToMessage().messageId(),
                    "You can safely delete this message from the chat.");
        }
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
