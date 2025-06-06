package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.domain.exceptions.UserFriendlyException;
import com.dev.user_transaction_management_system.infrastructure.web.response.HttpResponse;
import com.dev.user_transaction_management_system.use_case.authenticate_user.AuthenticateUser;
import com.dev.user_transaction_management_system.use_case.register_user_account.RegisterUserAccount;
import com.dev.user_transaction_management_system.use_case.authenticate_user.LoginRequest;
import com.dev.user_transaction_management_system.use_case.register_user_account.UserRegistrationRequest;
import com.dev.user_transaction_management_system.use_case.authenticate_user.UserAuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthenticateUser authenticateUser;

    private final RegisterUserAccount registerUserAccount;

    public AuthController(AuthenticateUser authenticateUser, RegisterUserAccount registerUserAccount) {
        this.authenticateUser = authenticateUser;
        this.registerUserAccount = registerUserAccount;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody UserRegistrationRequest request) {
        try {
            registerUserAccount.register(request);
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString()).
                    data(request)
                    .message("User registered")
                    .status(HttpStatus.CREATED)
                    .statusCode(HttpStatus.CREATED.value())
                    .build();
            return ResponseEntity.created(new URI("")).body(response);
        }catch (IllegalArgumentException | UserFriendlyException e){
            final HttpResponse response = HttpResponse.builder().timestamps(LocalDateTime.now().toString())
                    .status(HttpStatus.BAD_REQUEST)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
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
