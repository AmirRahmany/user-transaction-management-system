package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.DepositRequestBuilder.aDepositRequest;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class DepositingMoneyControllerTest extends BankAccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountTestUtil userAccountUtil;

    private UserEntity entity;
    private String token;

    @BeforeEach
    void setUp() throws Exception {
        String username="amir@gmail.com";
        String password = "@Abcd137845";

        entity = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password));

        LoginRequest loginRequest = new LoginRequest(username,password);
        final ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        final MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final JSONObject json = new JSONObject(contentAsString);
        token = "Bearer " + json.get("token");
    }

    @Test
    void deposit_transaction_successfully() throws Exception {
        final BankAccount from =havingOpened(anAccount().withUserId(entity.getId())
                .withAccountNumber("0300654789123").withBalance(500));

        final BankAccount to = havingOpened(anAccount().withAccountNumber("0300456574853")
                .withAccountId(321)
                .withBalance(140)
                .withUserId(UUID.randomUUID().toString()));

        final LocalDateTime createdAt = of(2025, 5, 4, 14, 30, 0);

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .withCreatedAt(createdAt).initiate();

        mockMvc.perform(post("/api/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",token)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());


        Optional<BankAccountEntity> savedToAccount = accountRepository.findByAccountNumber(to.accountNumber());
        Optional<BankAccountEntity> savedFromAccount = accountRepository.findByAccountNumber(from.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedFromAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.get().getBalance()).isEqualTo(200);
    }
}
