package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.UserMapper;
import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@Transactional
public class UserAccountTestUtil {

    private final UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivatingUserAccount activatingUserAccount;

    @Autowired
    protected ObjectMapper objectMapper;


    private final UserAccountRegistrationTestHelper registrationTestHelper;

    public UserAccountTestUtil(UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {

        this.registrationTestHelper = new UserAccountRegistrationTestHelper(userRepository, passwordEncoder);
        userMapper = new UserMapper();
    }

    public String signIn(String username, String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            final ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists());

            final MvcResult mvcResult = resultActions.andDo(log()).andReturn();
            final String contentAsString = mvcResult.getResponse().getContentAsString();

            final JSONObject json = new JSONObject(contentAsString);

            final String token = "Bearer " + json.get("token");

            return token;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to authenticate test user: " + username);
        }

    }

    public User havingRegistered(UserFakeBuilder userFakeBuilder) {
        return userMapper.toDomain(registrationTestHelper.havingRegistered(userFakeBuilder));
    }

    public void activateUserAccount(String username) {
        activatingUserAccount.activate(username);
    }


}
