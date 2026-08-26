package com.tensai.cms.telegram.api;

import org.springframework.core.io.Resource;

public interface TelegramService {
    Resource getFile(String fileId);
}
