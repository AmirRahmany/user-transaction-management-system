package com.dev.user_transaction_management_system.use_case.authenticate_user;

import com.dev.user_transaction_management_system.infrastructure.util.jwt.JwtTokenUtils;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
// TODO this class should be refactored, because doesn't apply dependency rule
public class AuthenticateUser {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtils jwtUtils;

    public AuthenticateUser(AuthenticationManager authenticationManager, JwtTokenUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
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
