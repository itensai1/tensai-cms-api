package com.tensai.cms.auth.internal.service;

import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;

public interface AuthService {
    String login(String username, String password);

    String generateResetPasswordUrl(Long telegramUserId);

    void resetPassword(ResetPasswordRequest request, String token);

    String registerNewTelegramUser(Long telegramUserId, Long telegramGroupId, String username, String firstName, String lastName, boolean isAdmin);

    boolean isExistingUser(Long telegramUserId);

    String registerOldTelegramUser(Long telegramUserId, Long telegramGroupId, String username, String firstName, String lastName, boolean isAdmin);

    void changeAdminStatus(Long telegramUserId, boolean isAdmin);
}
