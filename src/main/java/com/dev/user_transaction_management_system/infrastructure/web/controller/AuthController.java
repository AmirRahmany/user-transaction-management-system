package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisteringUserAccount registeringUserAccount;

    public AuthController(RegisteringUserAccount registeringUserAccount) {
        this.registeringUserAccount = registeringUserAccount;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationRequest> register(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        try {
            registeringUserAccount.register(userRegistrationRequest);
            return ResponseEntity.ok(userRegistrationRequest);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
