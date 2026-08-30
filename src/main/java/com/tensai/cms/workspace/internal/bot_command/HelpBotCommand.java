package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;
import com.tensai.cms.workspace.internal.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HelpBotCommand implements BotCommand {
    private final List<BotCommand> commands;
    private final ApplicationEventPublisher publisher;
    private final DraftService draftService;

    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public String getDescription() {
        return "show help message";
    }

    @Override
    public String getUsage() {
        return "send " + getCommand() + " to show this message";
    }

    @Override
    public ChatType getChatType() {
        return ChatType.TOPIC;
    }

    @Override
    public void handle(CreateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        String message = formatMessage(commands);
        message(event.chatId(), event.messageThreadId(), null, message);
    }

    @Override
    public void handle(UpdateMessageEvent event, String text) {
        if (!getChatType().equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
        String message = formatMessage(commands);
        draftService.deleteDraftBlock(event.chatId(), event.messageThreadId(), event.messageId());
        message(event.chatId(), event.messageThreadId(), null, message);
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

    public String formatMessage(List<BotCommand> cmds) {
        return cmds.stream()
                .filter(cmd -> cmd.getChatType().equals(getChatType()))
                .map(cmd ->
                        "* %s : %s \nUsage: %s ".formatted(
                                cmd.getCommand(),
                                cmd.getDescription(),
                                cmd.getUsage()
                        ))
                .collect(Collectors.joining("\n------ \n",
                        "Bot Commands \n\n",
                        "\n\n* %s \n* %s".formatted(getCommand(), getUsage())));

    }
}
