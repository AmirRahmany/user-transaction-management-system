package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserActivationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import java.util.UUID;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class ActivatingUserAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountTestUtil userAccountUtil;


    private UserEntity entity;
    private String token;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        final String username = "amir@gmail.com";
        final String password = "@Abcd137728";
        entity = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password).withDisabledStatus());

        final LoginRequest loginRequest = new LoginRequest(username, password);

        final ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(loginRequest)));

        final MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        final String contentAsString = mvcResult.getResponse().getContentAsString();
        final JSONObject json = new JSONObject(contentAsString);

        this.token = "Bearer " + json.get("token");
    }

    @Test
    void activate_user_account_successfully() throws Exception {
        final String userId = entity.getId();
        final UserActivationRequest userActivationRequest = new UserActivationRequest(userId);
        mockMvc.perform(post("/api/user/activation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(userActivationRequest)))
                .andExpect(status().isOk());

        final Optional<UserEntity> userEntity = userRepository.findById(UserId.fromUUID(UUID.fromString(userId)));
        assertThat(userEntity).isPresent();

        assertThat(userEntity.get().getUserStatus()).isEqualTo(UserStatus.ENABLE);
    }
}
