package com.tensai.cms.telegram.internal.web;

import com.tensai.cms.telegram.internal.service.TelegramWebhookService;
import com.tensai.cms.telegram.internal.dto.CmsEventRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {
    private final TelegramWebhookService webhookService;

    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvent(@RequestBody CmsEventRequest request) {
        webhookService.publishEvent(request);
        return ResponseEntity.ok().build();
    }
}
