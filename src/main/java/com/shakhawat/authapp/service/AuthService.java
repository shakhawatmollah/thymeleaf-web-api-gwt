package com.shakhawat.authapp.service;

import com.shakhawat.authapp.config.JwtService;
import com.shakhawat.authapp.dto.LoginRequest;
import com.shakhawat.authapp.dto.LoginResponse;
import com.shakhawat.authapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
       // var refreshToken = jwtService.generateRefreshToken(user);
        return LoginResponse.builder()
                .accessToken(jwtToken)
              //  .refreshToken(refreshToken) // Refresh token will be added in controller
                .build();
    }

}
