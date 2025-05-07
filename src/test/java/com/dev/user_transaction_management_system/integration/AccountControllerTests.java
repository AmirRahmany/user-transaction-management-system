package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.use_case.UserRegistration;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.AccountResponse;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
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

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
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
        final User user = havingRegistered(aUser().withFirstName("Amir").withLastName("Rahmani"));

        final Optional<UserEntity> savedUser = userRepository.findByEmail(user.email());

        assertThat(savedUser).isPresent();
        final AccountRequest accountRequest = new AccountRequest(savedUser.get().getId(), 5000);

        final String response = mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        final AccountResponse accountResponse = objectMapper.readValue(response, AccountResponse.class);
        final AccountNumber accountNumber = AccountNumber.of(accountResponse.accountNumber());

        final Optional<AccountEntity> accountEntity = accountRepository.findByAccountNumber(accountNumber);

        assertThat(accountResponse).isNotNull();
        assertThat(accountEntity).isPresent();
        System.out.println(accountResponse);
    }

    private User havingRegistered(UserFakeBuilder userFakeBuilder) {
        final User user = userFakeBuilder.build();
        userRegistration.register(user);
        return user;
    }
}
