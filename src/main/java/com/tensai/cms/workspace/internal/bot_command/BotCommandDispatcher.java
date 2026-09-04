package com.tensai.cms.workspace.internal.bot_command;

import com.tensai.cms.telegram.api.events.CreateMessageEvent;
import com.tensai.cms.telegram.api.events.MessageEntity;
import com.tensai.cms.telegram.api.events.UpdateMessageEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BotCommandDispatcher {
    private final Map<String, BotCommand> handlers;

    public BotCommandDispatcher(List<BotCommand> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(
                        handler -> handler.getCommand().toLowerCase(),
                        Function.identity()
                ));
    }

    public boolean isUserCommand(List<MessageEntity> entities) {
        if (entities != null) {
            return entities.stream()
                    .anyMatch(entity -> entity.type().equals("bot_command"));
        }
        return false;
    }

    public void dispatch(CreateMessageEvent event) {
        ParsedCommand parsedCommand = parse(event.entities(), event.text());
        if (parsedCommand == null) return;

        Optional.ofNullable(handlers.getOrDefault(parsedCommand.name().toLowerCase(), handlers.get("/help")))
                .ifPresent(cmd -> cmd.handle(event, parsedCommand.arg));

    }

    public void dispatch(UpdateMessageEvent event) {
        ParsedCommand parsedCommand = parse(event.entities(), event.text());
        if (parsedCommand == null) return;

        Optional.ofNullable(handlers.get(parsedCommand.name().toLowerCase()))
                .ifPresent(cmd -> cmd.handle(event, parsedCommand.arg));
    }

    private ParsedCommand parse(List<MessageEntity> entities, String text) {
        int atSign = text.indexOf('@');
        return entities.stream().filter(entity -> entity.type().equals("bot_command"))
                .findFirst().map(e -> new ParsedCommand(
                        text.substring(e.offset(), atSign < 0 ? e.offset() + e.length() : atSign),
                        text.substring(e.offset() + e.length()).strip()
                )).orElse(null);
    }

    private record ParsedCommand(String name, String arg) {
    }
}
