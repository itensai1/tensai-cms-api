package com.tensai.cms.auth.internal.callback;

import com.tensai.cms.auth.internal.service.AuthService;
import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.commands.*;
import com.tensai.cms.telegram.api.events.CallbackQueryEvent;
import com.tensai.cms.telegram.api.options.OptionRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthCallbackHandler {
    private final ApplicationEventPublisher publisher;
    private final AuthService authService;

    public void dispatch(CallbackQueryEvent event) {
        String[] data = event.data().split(":");
        if (!data[0].equals(OptionRoot.AUTH.value())) return;

        switch (data[1].toLowerCase()) {
            case "reset_password" -> resetPassword(event, data[2]);
            default -> throw new CustomException("Unknown Callback Event");
        }
    }

    private void resetPassword(CallbackQueryEvent event, String choice) {
        if (!ChatType.PRIVATE.equals(ChatType.evaluate(event.chatId(), event.messageThreadId())))
            return;

        if (choice.equalsIgnoreCase("click")) {
            try {
                String url = authService.generateResetPasswordUrl(event.chatId());

                Button btn = Button.builder().text("URL").type(ButtonType.URL).value(url).build();
                publisher.publishEvent(
                        SendMessageCommand.builder()
                                .chatId(event.chatId())
                                .text("Use this URL to reset your password.")
                                .keyboard(new Keyboard(List.of(List.of(btn)))).build()
                );

            } catch (CustomException e) {
                if (e.getCode() == 404) {
                    publisher.publishEvent(
                            AnswerCallbackCommand.builder()
                                    .callbackQueryId(event.callbackQueryId())
                                    .text("Account not found, add bot as an admin to a group first").build()
                    );
                }
            }
            publisher.publishEvent(
                    DeleteMessageCommand.builder().chatId(event.chatId()).messageId(event.messageId()).build()
            );
        }
    }
}
