package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.BankAccountActivationRequest;
import com.dev.user_transaction_management_system.use_case.dto.LoginRequest;
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
import java.util.UUID;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ActivatingBankAccountControllerTests extends BankAccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private UserAccountTestUtil userAccountUtil;


    private String token;
    private UserEntity entity;

    @BeforeEach
    void setUp() throws Exception {
        String username="amir@gmail.com";
        String password="@Abcd137854";
        entity = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password));

        LoginRequest loginRequest = new LoginRequest(username,password);
        final ResultActions resultActions = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));

        final MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        final String contentAsString = mvcResult.getResponse().getContentAsString();

        final JSONObject json = new JSONObject(contentAsString);

        token = "Bearer "+ json.get("token");
    }

    @Test
    void activate_user_bank_account_successfully() throws Exception {
        final BankAccount bankAccount = havingOpened(anAccount().withUserId(entity.getId()));
        final String accountNumber = bankAccount.accountNumberAsString();

        final BankAccountActivationRequest activationRequest = new BankAccountActivationRequest(accountNumber);

        mockMvc.perform(post("/api/account/activate")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                .content(objectMapper.writeValueAsString(activationRequest)))
                .andExpect(status().isOk());

        final Optional<BankAccountEntity> savedBankAccount =
                accountRepository.findByAccountNumber(bankAccount.accountNumber());

        assertThat(savedBankAccount).isPresent();
        assertThat(savedBankAccount.get().getStatus()).isEqualTo(AccountStatus.ENABLE);
    }
}
