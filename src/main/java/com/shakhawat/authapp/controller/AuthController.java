package com.shakhawat.authapp.controller;

import com.shakhawat.authapp.config.JwtService;
import com.shakhawat.authapp.dto.ApiResponse;
import com.shakhawat.authapp.dto.LoginRequest;
import com.shakhawat.authapp.dto.LoginResponse;
import com.shakhawat.authapp.dto.TokenRefreshRequest;
import com.shakhawat.authapp.entity.RefreshToken;
import com.shakhawat.authapp.entity.User;
import com.shakhawat.authapp.exception.TokenRefreshException;
import com.shakhawat.authapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody LoginRequest request) {
        LoginResponse response = authService.authenticate(request);
        // Get user details to create refresh token
        User user = (User) userService.loadUserByUsername(request.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        response.setTokenType("Bearer");
        // Update response with refresh token
        response.setRefreshToken(refreshToken.getToken());
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("success")
                        .message("User authenticated successfully")
                        .data(response)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);

                    // Invalidate the old refresh token
                    refreshTokenService.deleteByUserId(user.getId());

                    // Create a new refresh token
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

                    return ResponseEntity.ok(
                            ApiResponse.builder()
                                    .status("success")
                                    .message("Token refreshed successfully")
                                    .data(LoginResponse.builder()
                                            .accessToken(token)
                                            .refreshToken(newRefreshToken.getToken())
                                            .tokenType("Bearer")
                                            .build())
                                    .build()
                    );
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser(HttpServletRequest httpServletRequest, @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        if(tokenRefreshRequest.getRefreshToken() == null && SecurityUtils.getCurrentUserId() != null) {
            refreshTokenService.deleteByUserId(SecurityUtils.getCurrentUserId());
        } else {
            refreshTokenService.findByToken(tokenRefreshRequest.getRefreshToken())
                    .ifPresent(refreshToken -> {
                        refreshTokenService.deleteByUserId(refreshToken.getUser().getId());
                    });
        }

        String token = jwtService.resolveToken(httpServletRequest);

        var data = "";

        if (token != null && jwtService.validateToken(token)) {
            tokenBlacklistService.blacklistToken(token);

            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status("success")
                            .message("Log out successful")
                            .data(data)
                            .build()
            );
        }else {
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status("ERROR")
                            .message("Invalid token")
                            .data(data)
                            .build()
            );
        }
    }

}
