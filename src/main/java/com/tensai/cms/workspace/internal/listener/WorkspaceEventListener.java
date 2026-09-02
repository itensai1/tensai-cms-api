package com.tensai.cms.workspace.internal.listener;

import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.events.*;
import com.tensai.cms.telegram.api.options.OptionRoot;
import com.tensai.cms.workspace.internal.callback.WorkspaceCallbackHandler;
import com.tensai.cms.workspace.internal.service.DraftService;
import com.tensai.cms.workspace.internal.bot_command.BotCommandDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class WorkspaceEventListener {
    private final BotCommandDispatcher dispatcher;
    private final DraftService draftService;
    private final WorkspaceCallbackHandler callbackHandler;

    @ApplicationModuleListener
    public void on(CreateMessageEvent event) throws IOException {
        if (dispatcher.isUserCommand(event.entities())) {
            dispatcher.dispatch(event);
        } else {
            if (!ChatType.TOPIC.equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
            draftService.addDraftBlock(
                    event.chatId(),
                    event.messageThreadId(),
                    event.messageId(),
                    event.text(),
                    event.media()
            );
        }
    }

    @ApplicationModuleListener
    public void on(UpdateMessageEvent event) throws IOException {
        if (dispatcher.isUserCommand(event.entities())) {
            dispatcher.dispatch(event);
        } else {
            if (!ChatType.TOPIC.equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))) return;
            draftService.updateDraftBlock(
                    event.chatId(),
                    event.messageThreadId(),
                    event.messageId(),
                    event.text(),
                    event.media()
            );
        }
    }

    @ApplicationModuleListener
    void on(CreateTopicEvent event) {
        draftService.CreateDraft(event.chatId(), event.messageThreadId(), event.topicName());
    }

    @ApplicationModuleListener
    void on(UpdateTopicEvent event) {
        draftService.updateDraftTitle(event.chatId(), event.messageThreadId(), event.topicName());
    }

    @ApplicationModuleListener
    void on(CallbackQueryEvent event) {
        if (!OptionRoot.WORKSPACE.equals(OptionRoot.fromValue(event.data().split(":")[0]))) return;
        callbackHandler.dispatch(event);
    }
}
