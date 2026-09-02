package com.tensai.cms.workspace.internal.listener;

import com.tensai.cms.auth.api.UserQueryService;
import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.*;
import com.tensai.cms.telegram.api.options.OptionRoot;
import com.tensai.cms.workspace.internal.callback.WorkspaceCallbackHandler;
import com.tensai.cms.workspace.internal.service.DraftService;
import com.tensai.cms.workspace.internal.bot_command.BotCommandDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class WorkspaceEventListener {
    private final BotCommandDispatcher dispatcher;
    private final DraftService draftService;
    private final WorkspaceCallbackHandler callbackHandler;
    private final UserQueryService userQueryService;
    private final ApplicationEventPublisher publisher;

    @ApplicationModuleListener
    public void on(CreateMessageEvent event) throws IOException {
        if (ChatType.TOPIC.equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))
                && !userQueryService.isAdminBot(event.chatId())) {
            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.chatId())
                            .text("Bot must be admin, promote it first then send your message again.").build()
            );
            return;
        }

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
        if (!ChatType.TOPIC.equals(ChatType.evaluate(event.chatId(), event.messageThreadId()))
                && !userQueryService.isAdminBot(event.chatId())) {
            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.chatId())
                            .text("Bot must be admin, promote it first then update your message again.").build()
            );
            return;
        }

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
        if (!userQueryService.isAdminBot(event.chatId())) {
            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.chatId())
                            .text("Bot must be admin, promote it first then create another topic.").build()
            );
            return;
        }
        draftService.CreateDraft(event.chatId(), event.messageThreadId(), event.topicName());
    }

    @ApplicationModuleListener
    void on(UpdateTopicEvent event) {
        if (!userQueryService.isAdminBot(event.chatId())) {
            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.chatId())
                            .text("Bot must be admin, promote it first then change topic name again.").build()
            );
            return;
        }
        draftService.updateDraftTitle(event.chatId(), event.messageThreadId(), event.topicName());
    }

    @ApplicationModuleListener
    void on(CallbackQueryEvent event) {
        if (!OptionRoot.WORKSPACE.equals(OptionRoot.fromValue(event.data().split(":")[0]))) return;
        callbackHandler.dispatch(event);
    }
}
