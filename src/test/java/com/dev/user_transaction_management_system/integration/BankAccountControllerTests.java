package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;
import com.dev.user_transaction_management_system.use_case.dto.OpeningAccountResponse;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private UserAccountTestUtil userAccountUtil;

    private Object token;
    private UserEntity user;


    @BeforeEach
    void setUp() throws Exception {
        String username="amir@gmail.com";
        String password = "@Abcd13785";
        user = userAccountUtil.havingRegistered(aUser().withEnabledStatus().withEmail(username).withPassword(password));

        token = userAccountUtil.signIn(username, password);
    }

    @Test
    void open_an_account_successfully() throws Exception {
        userAccountUtil.activateUserAccount(user.getId());
        final AccountRequest accountRequest = new AccountRequest(user.getId(), 5000);

        final String response = mockMvc.perform(post("/api/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final OpeningAccountResponse openingAccountResponse = objectMapper.readValue(response, OpeningAccountResponse.class);
        final AccountNumber accountNumber = AccountNumber.of(openingAccountResponse.accountNumber());

        final Optional<BankAccountEntity> accountEntity = accountRepository.findByAccountNumber(accountNumber);

        assertThat(openingAccountResponse).isNotNull();
        assertThat(accountEntity).isPresent();
    }
}
