package com.tensai.cms.auth.internal.web;

import com.tensai.cms.auth.internal.service.AuthService;
import com.tensai.cms.auth.internal.web.dto.AuthResponse;
import com.tensai.cms.auth.internal.web.dto.LoginRequest;
import com.tensai.cms.auth.internal.web.dto.ResetPasswordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authentication", description = "Endpoints for user authentication and password management")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "User login",
            description = "Authenticates user credentials and returns a JWT access token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "400", description = "Invalid login payload or missing fields"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @SecurityRequirements // Marks endpoint as public in Swagger UI
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.username(), loginRequest.password());
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @Operation(
            summary = "Reset password",
            description = "Resets user password using a valid reset token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or expired/invalid token")
    })
    @SecurityRequirements // Marks endpoint as public in Swagger UI
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request,
            @Parameter(description = "Password reset verification token", required = true)
            @RequestParam String token
    ) {
        authService.resetPassword(request, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
