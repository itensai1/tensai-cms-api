package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.telegram.internal.web.dto.CmsEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramWebhookServiceImpl implements TelegramWebhookService {
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public void publishEvent(CmsEventRequest request) {
        switch (request.eventType()) {
            case REGISTER_USER -> publisher.publishEvent(request.registerUser());
            case CREATE_TOPIC -> publisher.publishEvent(request.createTopic());
            case UPDATE_TOPIC -> publisher.publishEvent(request.updateTopic());
            case CREATE_MESSAGE -> publisher.publishEvent(request.createMessage());
            case UPDATE_MESSAGE -> publisher.publishEvent(request.updateMessage());
            case CALLBACK_QUERY -> publisher.publishEvent(request.callbackQuery());
        }
    }
}
