package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
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

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class WithdrawingMoneyControllerTests extends BankAccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountTestUtil userAccountUtil;


    private String token;
    private UserEntity entity;

    @BeforeEach
    void setUp() throws Exception {
        String username = "amir@gamial.com";
        String password = "@Abcd137824";

        entity = userAccountUtil.havingRegistered(UserFakeBuilder.aUser()
                .withEnabledStatus().withEmail(username).withPassword(password));

        final LoginRequest loginRequest = new LoginRequest(username, password);

        final ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)));

        final MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final JSONObject json = new JSONObject(contentAsString);
        token = "Bearer " + json.get("token");
    }

    @Test
    void withdraw_money_transaction_successfully() throws Exception {
        final BankAccount account = havingOpened(anAccount().withUserId(entity.getId())
                .withAccountNumber("0300654789123").withBalance(500));

        final WithdrawalRequest withdrawalRequest =
                new WithdrawalRequest(300, account.accountNumberAsString(), "description");

        mockMvc.perform(post("/api/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(withdrawalRequest)))
                .andExpect(status().isOk());


        Optional<BankAccountEntity> savedToAccount = accountRepository.findByAccountNumber(account.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(200);
    }
}
