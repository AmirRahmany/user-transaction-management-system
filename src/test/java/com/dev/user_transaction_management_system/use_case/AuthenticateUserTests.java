package com.dev.user_transaction_management_system.use_case;


import com.dev.user_transaction_management_system.infrastructure.util.jwt.JwtTokenUtils;
import com.dev.user_transaction_management_system.use_case.authenticate_user.AuthenticateUser;
import com.dev.user_transaction_management_system.use_case.authenticate_user.LoginRequest;
import com.dev.user_transaction_management_system.use_case.authenticate_user.UserAuthenticationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserTests {


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtils tokenUtils;

    @InjectMocks
    private AuthenticateUser authenticateUser;


    @Test
    void authenticate_user_successfully() {
        final String username = "amir@gmail.com";
        final String password = "@Abcd1234";
        final LoginRequest loginRequest = new LoginRequest(username, password);

        UserDetails userDetails = new User(username, password, Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(tokenUtils.generateJwtTokenFromUserName(userDetails)).thenReturn("mock.jwt.token");

        UserAuthenticationResponse response = authenticateUser.authenticate(loginRequest);
        assertThat(response).isNotNull();
        assertThat(response.username()).isEqualTo(username);
        assertThat(response.token()).isEqualTo("mock.jwt.token");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenUtils).generateJwtTokenFromUserName(userDetails);
    }

    @Test
    void cannot_authenticate_user_when_authentication_fails() {
        final LoginRequest loginRequest = new LoginRequest("amir@gmail.com", "@Abcd1234");

        when(authenticationManager.authenticate(any())).thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->authenticateUser.authenticate(loginRequest));
    }

    @Test
    void cannot_authenticate_user_when_username_is_null() {
        final String username = null;
        final String password = "@Abcd1234";
        final LoginRequest loginRequest = new LoginRequest(username, password);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    UserDetails userDetails = new User(username, password, Collections.emptyList());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    when(authenticationManager.authenticate(any())).thenReturn(authentication);
                    authenticateUser.authenticate(loginRequest);
                });
    }

    @Test
    void cannot_authenticate_user_when_password_is_null() {
        final String username = "amir@gmail.com";
        final String password = null;

        final LoginRequest loginRequest = new LoginRequest(username, password);


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    UserDetails userDetails = new User(username, password, Collections.emptyList());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    when(authenticationManager.authenticate(any())).thenReturn(authentication);
                    authenticateUser.authenticate(loginRequest);
                });
    }
}
