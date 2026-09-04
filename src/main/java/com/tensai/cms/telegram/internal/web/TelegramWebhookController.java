package com.tensai.cms.telegram.internal.web;

import com.tensai.cms.telegram.internal.dto.CmsEventRequest;
import com.tensai.cms.telegram.internal.service.TelegramWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Telegram Webhooks", description = "Internal event ingestion endpoints for Telegram Gateway sync")
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramWebhookController {

    private final TelegramWebhookService webhookService;

    @Operation(
            summary = "Ingest Telegram event",
            description = "Internal webhook endpoint called by Telegram Gateway to ingest message posts, thread topics, media updates, or user registrations."
    )
    @SecurityRequirement(name = "telegramHeaderAuth") // Uses X-Internal-Secret header instead of global Bearer auth
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event processed and published successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid event payload"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid X-Internal-Secret security header")
    })
    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvent(@Valid @RequestBody CmsEventRequest request) {
        webhookService.publishEvent(request);
        return ResponseEntity.ok().build();
    }
}
