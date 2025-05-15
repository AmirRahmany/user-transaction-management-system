package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.infrastructure.util.jwt.JwtTokenUtils;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserAuthenticationResponse;
import io.jsonwebtoken.lang.Assert;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticateUser {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtUtils;

    public AuthenticateUser(AuthenticationManager authenticationManager, JwtTokenUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public UserAuthenticationResponse authenticate(LoginRequest request) {
        Assert.notNull(request,"login request cannot be null");

        Authentication authentication;
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (RuntimeException exception) {
            throw  new IllegalArgumentException("your username or password is wrong");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String token = jwtUtils.generateJwtTokenFromUserName(userDetails);
        return new UserAuthenticationResponse(token, userDetails.getUsername());
    }
}
