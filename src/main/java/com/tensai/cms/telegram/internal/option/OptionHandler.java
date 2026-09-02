package com.tensai.cms.telegram.internal.option;

import com.tensai.cms.shared.model.ChatType;
import com.tensai.cms.telegram.api.commands.Button;
import com.tensai.cms.telegram.api.commands.Keyboard;
import com.tensai.cms.telegram.api.commands.SendMessageCommand;
import com.tensai.cms.telegram.api.options.OptionBtn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OptionHandler {
    private final List<OptionBtn> optionBtns;
    private final ApplicationEventPublisher publisher;

    public void handleSettings(Long chatId, Long topicId, ChatType chatType) {
        List<List<Button>> buttons = new ArrayList<>();
        for (OptionBtn btn : optionBtns.stream()
                .filter(b -> b.getType().matches(chatType)).toList()) {
            if (buttons.isEmpty() || buttons.getLast().size() == 2)
                buttons.addLast(new ArrayList<>());
            buttons.getLast().add(btn.getButton());
        }
        Keyboard keyboard = buttons.getFirst().isEmpty() ? null : new Keyboard(buttons);
        publisher.publishEvent(
                SendMessageCommand.builder()
                        .chatId(chatId).text(">> Settings <<")
                        .messageThreadId(topicId)
                        .keyboard(keyboard).build()
        );
    }

}
