package com.tensai.cms.auth.internal.web;

import com.tensai.cms.auth.internal.service.AuthService;
import com.tensai.cms.auth.internal.web.dto.AuthResponse;
import com.tensai.cms.auth.internal.web.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
