package com.tensai.cms.telegram.internal.listener;

import com.tensai.cms.telegram.api.options.SettingsOptionEvent;
import com.tensai.cms.telegram.internal.option.OptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramOptionListener {
    private final OptionHandler optionHandler;

    @ApplicationModuleListener
    public void on(SettingsOptionEvent event) {
        optionHandler.handleSettings(event.chatId(), event.topicId(), event.type());
    }

}
