package com.tensai.cms.auth.internal.web;

import com.tensai.cms.auth.internal.service.AuthService;
import com.tensai.cms.auth.internal.web.dto.AuthResponse;
import com.tensai.cms.auth.internal.web.dto.LoginRequest;
import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        String token = authService.login(loginRequest.username(), loginRequest.password());

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            @RequestParam String token) {
        authService.resetPassword(request, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
