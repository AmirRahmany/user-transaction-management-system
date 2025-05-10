package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.UserRegistration;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistration userRegistration;

    public AuthController(UserRegistration userRegistration) {
        this.userRegistration = userRegistration;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationRequest> register(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        try {
            userRegistration.register(userRegistrationRequest);
            return ResponseEntity.ok(userRegistrationRequest);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
