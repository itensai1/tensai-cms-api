package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;
import com.tensai.cms.telegram.api.options.SettingsOptionEvent;
import com.tensai.cms.workspace.internal.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettingsBotCommand implements BotCommand {
    private final ApplicationEventPublisher publisher;
    private final DraftService draftService;

    @Override
    public String getCommand() {
        return "/settings";
    }

    @Override
    public String getDescription() {
        return "bot settings";
    }

    @Override
    public String getUsage() {
        return "send " + getCommand();
    }

    @Override
    public ChatType getChatType() {
        return ChatType.ANY;
    }


    @Override
    public void handle(CreateMessageEvent event, String text) {
        publisher.publishEvent(
                SettingsOptionEvent.builder()
                        .chatId(event.chatId()).topicId(event.messageThreadId())
                        .type(ChatType.evaluate(event.chatId(), event.messageThreadId())).build()
        );
    }

    @Override
    public void handle(UpdateMessageEvent event, String text) {
        if (ChatType.evaluate(event.chatId(), event.messageThreadId()).equals(ChatType.TOPIC))
            draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.messageId());
        publisher.publishEvent(
                SettingsOptionEvent.builder()
                        .chatId(event.chatId()).topicId(event.messageThreadId())
                        .type(ChatType.evaluate(event.chatId(), event.messageThreadId())).build()
        );
    }
}
