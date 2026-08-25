package com.tensai.cms.telegram.internal.service;

import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.telegram.api.TelegramService;
import com.tensai.cms.telegram.internal.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final RestClient telegramClient;
    private final TelegramProperties properties;

    @Override
    public Resource getFile(String fileId) {
        try {
            return telegramClient.get()
                    .uri(properties.endpoints().getFile() + "/{fileId}", fileId)
                    .retrieve().body(Resource.class);

        } catch (Exception e) {
            throw new CustomException(
                    "Unexpected error during getting file '%s': %s"
                            .formatted(fileId, e.getMessage()), e);
        }
    }
}
