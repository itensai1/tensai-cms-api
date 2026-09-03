package com.tensai.cms.auth.internal.service;

import com.tensai.cms.auth.api.UserInfo;
import com.tensai.cms.auth.api.UserQueryService;
import com.tensai.cms.auth.internal.config.SecurityProperties;
import com.tensai.cms.auth.internal.entity.CmsUserDetails;
import com.tensai.cms.auth.internal.entity.TokenPurpose;
import com.tensai.cms.auth.internal.entity.User;
import com.tensai.cms.auth.internal.entity.UserRole;
import com.tensai.cms.auth.internal.repository.UserRepo;
import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;
import com.tensai.cms.shared.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserQueryService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepo userRepo;
    private final SecurityProperties properties;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateAccessToken(TokenPurpose.LOGIN.purposeClaim(), userDetails.getUsername());

            return token;

        } catch (Exception e) {
            throw new CustomException(401, "Bad credentials", e);
        }
    }

    @Override
    public String generateResetPasswordUrl(Long telegramUserId) {

        User user = userRepo.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new CustomException(404, "User not found"));

        return generateResetPasswordUrl(user.getUsername());
    }

    private String generateResetPasswordUrl(String username) {
        String token = jwtUtil.generateAccessToken(TokenPurpose.RESET_PASSWORD.purposeClaim(), username, 15);

        String url = "%s?token=%s".formatted(properties.resetPasswordPath(), token);
        return url;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request, String token) {

        if (!jwtUtil.validateAccessToken(token, TokenPurpose.RESET_PASSWORD)) {
            throw new CustomException(401, "Invalid token");
        }

        String username = jwtUtil.extractSubject(token);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new CustomException(404, "User not found"));

        user.setPassword(passwordEncoder.encode(request.password()));
        userRepo.save(user);
    }

    @Override
    @Transactional
    public String registerNewTelegramUser(Long telegramUserId, Long telegramGroupId, String username, String firstName, String lastName, boolean isAdmin) {

        if (!isAdmin)
            throw new CustomException(400, "Bot must be admin");
        else if (userRepo.existsByTelegramUserId(telegramUserId))
            throw new CustomException(400, "Telegram user already exists");

        User user = User.builder()
                .telegramUserId(telegramUserId)
                .username(username != null ? username : "usr" + telegramUserId)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.USER).telegramGroupId(telegramGroupId)
                .firstName(firstName).lastName(lastName).adminBot(true).build();
        userRepo.save(user);

        return user.getUsername() + " " + generateResetPasswordUrl(user.getUsername());
    }

    @Override
    public boolean isExistingUser(Long telegramUserId) {
        return userRepo.existsByTelegramUserId(telegramUserId);
    }

    @Override
    @Transactional
    public String registerOldTelegramUser(Long telegramUserId, Long telegramGroupId, String username, String firstName, String lastName, boolean isAdmin) {
        User user = userRepo.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new CustomException(404, "User not found"));

        Long oldGroupId = user.getTelegramGroupId();

        user.setTelegramGroupId(telegramGroupId);
        user.setAdminBot(isAdmin);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (username != null) user.setUsername(username);
        userRepo.save(user);
        if (!oldGroupId.equals(telegramGroupId)) {
            return oldGroupId.toString();
        }
        return null;
    }

    @Override
    @Transactional
    public void changeAdminStatus(Long telegramUserId, boolean isAdmin) {
        User user = userRepo.findByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new CustomException(404, "User not found"));
        user.setAdminBot(isAdmin);
        userRepo.save(user);
    }

    @Override
    public UUID getUserIdByTelegramGroupId(Long telegramGroupId) {
        return userRepo.findIdByTelegramGroupId(telegramGroupId);
    }

    @Override
    public Map<UUID, UserInfo> getUserInfoByIds(Set<UUID> userIds) {
        if (userIds == null) return Map.of();
        return userRepo.findAllByIdIn(userIds).stream()
                .collect(Collectors.toMap(u -> u.getId(),
                        u -> new UserInfo(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName()))
                );
    }

    @Override
    public boolean isAdminBot(Long telegramGroupId) {
        return userRepo.existsByTelegramGroupIdAndAdminBotTrue(telegramGroupId);
    }

    @Override
    public UserInfo getCurrentUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        if (auth.getPrincipal() instanceof CmsUserDetails userDetails) {
            var user = userDetails.getUser();
            if (user == null) return null;
            return new UserInfo(
                    user.getId(), userDetails.getUsername(),
                    user.getFirstName(), user.getLastName()
            );
        }
        return null;
    }

    @Override
    public UUID getCurrentUserId() {
        UserInfo info = getCurrentUserInfo();
        if (info == null) return null;
        return info.id();
    }
}
