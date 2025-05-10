package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.use_case.ActivatingUserAccount;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
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
@ActiveProfiles("test")
@Transactional
class BankAccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisteringUserAccount registeringUserAccount;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivatingUserAccount activatingUserAccount;

    @Test
    void open_an_account_successfully() throws Exception {
        final UserEntity user = havingRegistered(aUser().withEnabledStatus().withFirstName("Amir").withLastName("Rahmani"));

        final AccountRequest accountRequest = new AccountRequest(user.getId(), 5000);

        final String response = mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final OpeningAccountResponse openingAccountResponse = objectMapper.readValue(response, OpeningAccountResponse.class);
        final AccountNumber accountNumber = AccountNumber.of(openingAccountResponse.accountNumber());

        final Optional<AccountEntity> accountEntity = accountRepository.findByAccountNumber(accountNumber);

        assertThat(openingAccountResponse).isNotNull();
        assertThat(accountEntity).isPresent();
        System.out.println(openingAccountResponse);
    }

    private UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserRegistrationRequest user = userFakeBuilder.buildDTO();
        registeringUserAccount.register(user);
        final UserEntity entity = userRepository.findByEmail(user.email()).orElseThrow(CouldNotFindAccount::new);
        activatingUserAccount.activate(entity.getId());
        return entity;
    }
}
