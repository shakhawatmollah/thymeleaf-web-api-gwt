package com.shakhawat.authapp.controller;

import com.shakhawat.authapp.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public ResponseEntity<ApiResponse> publicEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("success")
                        .message("Public endpoint accessible to all")
                        .build()
        );
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> userEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("success")
                        .message("User endpoint accessible to authenticated users")
                        .build()
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> adminEndpoint() {
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .status("success")
                        .message("Admin endpoint accessible only to admins")
                        .build()
        );
    }
}
