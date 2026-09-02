package com.tensai.cms.telegram.api.options;

import com.tensai.cms.shared.model.ChatType;
import lombok.Builder;

@Builder
public record SettingsOptionEvent(
        Long chatId,
        Long topicId,
        ChatType type
) {
}
