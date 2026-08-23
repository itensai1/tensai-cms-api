package com.tensai.cms.auth.internal.service;

import com.tensai.cms.auth.internal.config.SecurityProperties;
import com.tensai.cms.auth.internal.entity.TokenPurpose;
import com.tensai.cms.auth.internal.entity.User;
import com.tensai.cms.auth.internal.repository.UserRepo;
import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;
import com.tensai.cms.shared.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
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

        String token = jwtUtil.generateAccessToken(TokenPurpose.RESET_PASSWORD.purposeClaim(), user.getUsername(), 15);

        String url = "%s?token=%s".formatted(properties.resetPasswordPath(), token);
        return url;
    }

    @Override
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

}
