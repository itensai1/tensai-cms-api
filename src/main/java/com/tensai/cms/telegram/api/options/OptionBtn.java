package com.tensai.cms.telegram.api.options;

import com.tensai.cms.telegram.api.commands.Button;
import com.tensai.cms.telegram.api.commands.ButtonType;
import com.tensai.cms.shared.model.ChatType;

public interface OptionBtn {
    OptionRoot getRoot();

    String getData();

    String getText();

    ChatType getType();

    default Button getButton() {
        return Button.builder()
                .text(getText())
                .type(ButtonType.CALLBACK)
                .value(getRoot().value() + ":" + getData())
                .build();
    }
}
