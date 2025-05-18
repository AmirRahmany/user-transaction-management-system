package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Tag("INTEGRATION")
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private UserAccountTestUtil accountTestUtil;

    @BeforeEach
    void setUp() {
        this.accountTestUtil = new UserAccountTestUtil(userRepository,passwordEncoder);
    }

    @Test
    void register_user_successfully() throws Exception {
        final String email = "amir.ssofi@gmail.com";
        final UserRegistrationRequest userRegistrationRequest = aUser()
                .withFirstName("amir")
                .withLastName("sofi")
                .withEmail(email)
                .withPassword("@Abcd123")
                .withPhoneNumber("09214567845").buildDTO();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isOk());

        final boolean isUserAlreadyExists = userRepository.isUserAlreadyExists(email);
        final Optional<UserEntity> user = userRepository.findByEmail(email);

        assertThat(isUserAlreadyExists).isTrue();
        assertThat(user).isPresent();
        assertThat(user.get().getFirstName()).isEqualTo(userRegistrationRequest.firstName());
    }

    @Test
    void sign_in_user_successfully() throws Exception {
        final String username = "amir@gmail.com";
        final String password = "@Abcd1234";
        accountTestUtil.havingRegistered(aUser().withEmail(username).withPassword(password));
        final LoginRequest loginRequest = new LoginRequest(username,password);

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username));
    }
}
