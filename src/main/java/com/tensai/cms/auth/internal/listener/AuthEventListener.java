package com.tensai.cms.auth.internal.listener;

import com.tensai.cms.auth.internal.callback.AuthCallbackHandler;
import com.tensai.cms.auth.internal.service.AuthService;
import com.tensai.cms.telegram.api.commands.Button;
import com.tensai.cms.telegram.api.commands.ButtonType;
import com.tensai.cms.telegram.api.commands.Keyboard;
import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.events.CallbackQueryEvent;
import com.tensai.cms.telegram.api.events.RegisterUserEvent;
import com.tensai.cms.telegram.api.options.OptionRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthEventListener {
    private final AuthService authService;
    private final ApplicationEventPublisher publisher;
    private final AuthCallbackHandler callbackHandler;

    @ApplicationModuleListener
    public void on(RegisterUserEvent event) {
        boolean isExistingUser = authService.isExistingUser(event.telegramUserId());
        if (event.isAdmin() && !isExistingUser) { // new user
            String token = authService.registerNewTelegramUser
                    (event.telegramUserId(), event.telegramGroupId(), event.username(), event.firstName(),
                            event.lastName(), true);
            String username = token.split(" ")[0];
            String url = token.split(" ")[1];
            Button btn = Button.builder().text("URL").type(ButtonType.URL).value(url).build();
            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.telegramGroupId())
                            .text("""
                                     Hi %s, welcome to Tensai CMS.
                                    
                                     use this url to reset your password within 15 minutes and don't share it with anyone.
                                     you can login with username and password after that
                                    
                                     Your username: %s
                                    
                                    """.formatted(event.firstName(), username))
                            .keyboard(new Keyboard(List.of(List.of(btn)))).build()
            );

        } else if (event.isAdmin()) { // old user added bot again
            String groupId = authService.registerOldTelegramUser
                    (event.telegramUserId(), event.telegramGroupId(), event.username(), event.firstName(),
                            event.lastName(), true);

            String message = groupId == null ? "" : """
                    This group https://t.me/c/%s/1 is no longer managed by our bot, but you can still find its blogs;
                    Add the bot to it again as an admin to edit its blogs.""".formatted(groupId.substring(4));

            publisher.publishEvent(
                    SendMessageCommand.builder()
                            .chatId(event.telegramGroupId())
                            .text("Hi %s, welcome to Tensai CMS again\n".formatted(event.firstName()) + message).build()
            );

        } else if (isExistingUser) { // revoke admin role
            authService.changeAdminStatus(event.telegramUserId(), false);
        }
    }

    @ApplicationModuleListener
    void on(CallbackQueryEvent event) {
        if (!OptionRoot.AUTH.equals(OptionRoot.fromValue(event.data().split(":")[0]))) return;
        callbackHandler.dispatch(event);
    }
}
