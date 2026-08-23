package com.tensai.cms.auth.internal.service;

import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;

public interface AuthService {
    String login(String username, String password);

    String generateResetPasswordUrl(Long telegramUserId);

    void resetPassword(ResetPasswordRequest request, String token);
}
