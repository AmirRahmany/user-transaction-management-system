package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.dev.user_transaction_management_system.fake.UserFake.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_user_successfully() throws Exception {
        final String email = "amir.ssofi@gmail.com";
        final UserRegistrationRequest userRegistrationRequest = user()
                .withFirstName("amir")
                .withLastName("sofi")
                .withEmail(email)
                .withPassword("@Abcd123")
                .withPhoneNumber("09214567845").buildDTO();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isOk());

        assertThat(userRepository.isUserAlreadyExists(email)).isTrue();
        assertThat(userRepository.findByEmail(email).get().getFirstName()).isEqualTo(userRegistrationRequest.firstName());
    }

    @Test
    void cant_register_user_with_invalid_email() throws Exception {
        final UserRegistrationRequest userRegistrationRequest = user().withEmail("amir.com").buildDTO();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isBadRequest());
    }
}
