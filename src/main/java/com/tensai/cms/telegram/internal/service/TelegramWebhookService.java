package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.telegram.internal.web.dto.CmsEventRequest;

public interface TelegramWebhookService {
    void publishEvent(CmsEventRequest request);
}
