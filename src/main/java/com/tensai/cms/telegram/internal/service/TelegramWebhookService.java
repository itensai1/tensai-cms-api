package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.telegram.internal.dto.CmsEventRequest;

public interface TelegramWebhookService {
    void publishEvent(CmsEventRequest request);
}
