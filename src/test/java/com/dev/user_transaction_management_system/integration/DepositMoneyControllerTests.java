package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.DepositRequestBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
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

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
class DepositMoneyControllerTests extends BankAccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountTestUtil userAccountUtil;

    private User userAccount;
    private String token;

    @BeforeEach
    void setUp() {
        String username="amir@gmail.com";
        String password = "@Abcd137845";

        userAccount = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password));

        token = userAccountUtil.signIn(username, password);
    }

    @Test
    void init_deposit_money_request_successfully() throws Exception {
        final BankAccount to =havingOpened(anAccount().enabled().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final DepositRequest transferMoneyRequest = DepositRequestBuilder.aDepositRequest()
                .withAmount(300)
                .withAccountNumber(to.accountNumberAsString())
                .withDescription("transaction description")
                .initiate();

        mockMvc.perform(post("/api/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",token)
                        .content(objectMapper.writeValueAsString(transferMoneyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referenceNumber").exists())
                .andExpect(jsonPath("$.accountNumber").exists())
                .andExpect(jsonPath("$.createdAt").exists());


        Optional<BankAccountEntity> savedToAccount = accountRepository.findByAccountNumber(to.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(800);
    }
}
