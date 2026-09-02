package com.tensai.cms.auth.internal.option;

import com.tensai.cms.telegram.api.options.OptionBtn;
import com.tensai.cms.telegram.api.options.OptionRoot;
import com.tensai.cms.shared.model.ChatType;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordOptionBtn implements OptionBtn {
    @Override
    public OptionRoot getRoot() {
        return OptionRoot.AUTH;
    }

    @Override
    public String getData() {
        return "reset_password:click";
    }

    @Override
    public String getText() {
        return "Reset Password";
    }

    @Override
    public ChatType getType() {
        return ChatType.PRIVATE;
    }
}
