package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.dto.UserInformation;
import com.dev.user_transaction_management_system.fake.UserFake;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.dev.user_transaction_management_system.fake.UserFake.user;
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

    @Test
    void register_user_successfully() throws Exception {
        final UserInformation userInformation = user()
                .withFirstName("amir")
                .withLastName("sofi")
                .withEmail("amir.ssofi@gmail.com")
                .withPassword("@Abcd123")
                .withPhoneNumber("09214567845").buildDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInformation))).andExpect(status().isOk());
    }

    @Test
    void cant_register_user_with_invalid_email() throws Exception {
        final UserInformation userInformation = user().withEmail("amir.com").buildDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInformation))).andExpect(status().isBadRequest());
    }
}
