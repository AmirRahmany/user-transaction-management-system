package com.dev.user_transaction_management_system.infrastructure.web.controller;

import com.dev.user_transaction_management_system.infrastructure.util.jwt.JwtTokenUtils;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserAuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtUtils;

    private final RegisteringUserAccount registeringUserAccount;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtils jwtUtils,
                          RegisteringUserAccount registeringUserAccount) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.registeringUserAccount = registeringUserAccount;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegistrationRequest request) {
        try {
            registeringUserAccount.register(request);
            return ResponseEntity.ok().build();
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequest request){
        Authentication authentication;
        try {
            var authenticationToken =
                    new UsernamePasswordAuthenticationToken(request.username(), request.password());
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().build();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtUtils.generateJwtTokenFromUserName(userDetails);
        final UserAuthenticationResponse response = new UserAuthenticationResponse(token, userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
