package com.tensai.cms.workspace.internal.callback;

import com.tensai.cms.telegram.api.options.OptionBtn;
import com.tensai.cms.telegram.api.options.OptionRoot;
import com.tensai.cms.shared.model.ChatType;
import org.springframework.stereotype.Component;

@Component
public class DeleteBlogOptionBtn implements OptionBtn {
    @Override
    public OptionRoot getRoot() {
        return OptionRoot.WORKSPACE;
    }

    @Override
    public String getData() {
        return "delete_blog:click";
    }

    @Override
    public String getText() {
        return "Delete Blog";
    }

    @Override
    public ChatType getType() {
        return ChatType.TOPIC;
    }
}
