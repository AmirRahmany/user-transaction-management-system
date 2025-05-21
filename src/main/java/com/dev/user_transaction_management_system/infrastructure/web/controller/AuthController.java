package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.use_case.AuthenticateUser;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserAuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUser authenticateUser;

    private final RegisteringUserAccount registeringUserAccount;

    public AuthController(AuthenticateUser authenticateUser, RegisteringUserAccount registeringUserAccount) {
        this.authenticateUser = authenticateUser;
        this.registeringUserAccount = registeringUserAccount;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody UserRegistrationRequest request) {
        try {
            registeringUserAccount.register(request);
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data(Map.of("user", request))
                    .message("User registered")
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.created(new URI("")).body(response);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request) {
        try {
            final UserAuthenticationResponse response = authenticateUser.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
