package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.UserMapper;
import com.dev.user_transaction_management_system.test_builder.UserTestBuilder;
import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import com.dev.user_transaction_management_system.use_case.AuthenticateUser;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserAuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Component
@Transactional
public class UserAccountTestHelper {

    private final UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivatingUserAccount activatingUserAccount;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private AuthenticateUser authenticateUser;

    private final UserAccountRegistrationTestHelper registrationTestHelper;

    public UserAccountTestHelper(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {

        this.registrationTestHelper = new UserAccountRegistrationTestHelper(userRepository, passwordEncoder);
        this.userMapper = new UserMapper();
    }

    public String signIn(String username, String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);

            final UserAuthenticationResponse response = authenticateUser.authenticate(loginRequest);

            return "Bearer " + response.token();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to authenticate test user: " + username);
        }

    }

    public User havingRegistered(UserTestBuilder userTestBuilder) {
        return userMapper.toDomain(registrationTestHelper.havingRegistered(userTestBuilder));
    }

}
