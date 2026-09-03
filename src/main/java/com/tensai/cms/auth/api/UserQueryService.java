package com.tensai.cms.auth.api;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserQueryService {
    UUID getUserIdByTelegramGroupId(Long telegramGroupId);

    Map<UUID, UserInfo> getUserInfoByIds(Set<UUID> userIds);

    boolean isAdminBot(Long telegramGroupId);

    UserInfo getCurrentUserInfo();

    UUID getCurrentUserId();
}
