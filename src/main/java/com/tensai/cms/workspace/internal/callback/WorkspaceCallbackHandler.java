package com.tensai.cms.workspace.internal.callback;

import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.commands.*;
import com.tensai.cms.telegram.api.events.CallbackQueryEvent;
import com.tensai.cms.telegram.api.options.OptionRoot;
import com.tensai.cms.workspace.internal.service.DraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkspaceCallbackHandler {
    private final ApplicationEventPublisher publisher;
    private final DraftService draftService;

    public void dispatch(CallbackQueryEvent event) {
        String[] data = event.data().split(":");
        if (!data[0].equals(OptionRoot.WORKSPACE.value())) return;

        switch (data[1].toLowerCase()) {
            case "delete_blog" -> deleteBlog(event, data[2], data[0] + ":" + data[1]);
            default -> throw new CustomException("Unknown Callback Event");
        }
    }

    private void deleteBlog(CallbackQueryEvent event, String choice, String prefix) {
        if (!ChatType.TOPIC.equals(ChatType.evaluate(event.chatId(), event.messageThreadId())))
            return;

        switch (choice.toLowerCase()) {
            case "click" -> deleteBlogClick(event, prefix);
            case "ok" -> deleteBlogOk(event);
            case "no" -> deleteBlogNo(event);
        }
    }

    private void deleteBlogClick(CallbackQueryEvent event, String prefix) {
        Button btnOk = Button.builder().text("Are You Sure?")
                .type(ButtonType.CALLBACK).value(prefix + ":ok").build();
        Button btnNo = Button.builder().text("Discard")
                .type(ButtonType.CALLBACK).value(prefix + ":no").build();
        publisher.publishEvent(
                EditKeyboardCommand.builder()
                        .chatId(event.chatId()).messageId(event.messageId())
                        .keyboard(new Keyboard(List.of(List.of(btnOk), List.of(btnNo)))).build()
        );
    }

    private void deleteBlogOk(CallbackQueryEvent event) {
        publisher.publishEvent(
                DeleteTopicCommand.builder().chatId(event.chatId())
                        .messageThreadId(event.messageThreadId()).build()
        );
        draftService.deleteDraft(event.chatId(), event.messageThreadId());
    }

    private void deleteBlogNo(CallbackQueryEvent event) {
        publisher.publishEvent(
                DeleteMessageCommand.builder().chatId(event.chatId()).messageId(event.messageId()).build()
        );
    }
}
