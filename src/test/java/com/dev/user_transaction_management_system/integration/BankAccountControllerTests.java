package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Tag("INTEGRATION")
class BankAccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private UserAccountFixture userAccountFixture;

    private Object token;

    private User userAccount;


    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.givenActivatedSignedInUserWithToken();
        token = userAndToken.token();
        userAccount = userAndToken.user();
    }

    @Test
    void open_an_account_successfully() throws Exception {
        final AccountRequest accountRequest = new AccountRequest(userAccount.email(), 5000);
        final String response = mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var openingAccountResponse = objectMapper.readValue(response, OpeningAccountResponse.class);
        final AccountNumber accountNumber = AccountNumber.of(openingAccountResponse.accountNumber());
        final Optional<BankAccountEntity> accountEntity = accountRepository.findByAccountNumber(accountNumber);

        assertThat(openingAccountResponse).isNotNull();
        assertThat(accountEntity).isPresent();
    }
}
