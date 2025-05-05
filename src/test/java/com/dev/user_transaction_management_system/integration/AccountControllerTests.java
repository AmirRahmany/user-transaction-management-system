package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.application.UserRegistration;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.dto.AccountRequest;
import com.dev.user_transaction_management_system.model.UserEntity;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import com.dev.user_transaction_management_system.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;
import static com.dev.user_transaction_management_system.fake.UserFake.user;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRegistration userRegistration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void open_an_account_successfully() throws Exception {
        final User user = user().build();
        userRegistration.register(user);
        final Optional<UserEntity> savedUser = userRepository.findByEmail(user.email());
        assertThat(savedUser).isPresent();

        final AccountRequest accountRequest = new AccountRequest(savedUser.get().getId(),5000);

        mockMvc.perform(post("/api/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk());

    }
}
