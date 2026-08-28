package com.tensai.cms.auth.api;

import java.util.UUID;

public interface UserQueryService {
    UUID getUserIdByTelegramGroupId(Long telegramGroupId);
}
