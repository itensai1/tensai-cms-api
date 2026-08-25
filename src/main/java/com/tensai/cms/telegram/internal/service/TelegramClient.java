package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.telegram.internal.dto.CmsCommand;

public interface TelegramClient {
    void sendCommand(CmsCommand command);
}
