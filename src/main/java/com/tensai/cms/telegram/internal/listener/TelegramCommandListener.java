package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.telegram.api.commands.*;
import com.tensai.cms.telegram.internal.dto.CmsCommand;
import com.tensai.cms.telegram.internal.dto.CommandType;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TelegramCommandListener {
    private final TelegramClient telegramClient;

    @ApplicationModuleListener
    public void on(SendMessageCommand command) {
        telegramClient.sendCommand(
                CmsCommand.builder()
                        .commandType(CommandType.SEND_MESSAGE)
                        .sendMessage(command)
                        .build()
        );
    }

    @ApplicationModuleListener
    public void on(DeleteMessageCommand command) {
        telegramClient.sendCommand(
                CmsCommand.builder()
                        .commandType(CommandType.DELETE_MESSAGE)
                        .deleteMessage(command)
                        .build()
        );
    }

    @ApplicationModuleListener
    public void on(DeleteTopicCommand command) {
        telegramClient.sendCommand(
                CmsCommand.builder()
                        .commandType(CommandType.DELETE_TOPIC)
                        .deleteTopic(command)
                        .build()
        );
    }

    @ApplicationModuleListener
    public void on(EditKeyboardCommand command) {
        telegramClient.sendCommand(
                CmsCommand.builder()
                        .commandType(CommandType.EDIT_KEYBOARD)
                        .editKeyboard(command)
                        .build()
        );
    }

    @ApplicationModuleListener
    public void on(AnswerCallbackCommand command) {
        telegramClient.sendCommand(
                CmsCommand.builder()
                        .commandType(CommandType.ANSWER_CALLBACK)
                        .answerCallback(command)
                        .build()
        );
    }
}
