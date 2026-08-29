package com.tensai.cms.workspace.internal.bot_command;

public enum ChatType {
    PRIVATE,
    GROUP,
    TOPIC;

    public static ChatType evaluate(Long chatId, Long topicId) {
        if (chatId == null) return null;
        if (chatId > 0) return PRIVATE;
        if (topicId == null) return GROUP;
        return TOPIC;
    }
}
