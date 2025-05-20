package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
//@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class WithdrawingMoneyControllerTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountTestUtil userAccountUtil;

    @Autowired
    private BankAccountTestHelper bankAccountHelper;


    private String token;
    private User userAccount;

    @BeforeEach
    void setUp() {
        String username = "amir@gamial.com";
        String password = "@Abcd137824";

        userAccount = userAccountUtil.havingRegistered(UserFakeBuilder.aUser()
                .withEnabledStatus().withEmail(username).withPassword(password));

        token = userAccountUtil.signIn(username, password);
    }

    @Test
    void withdraw_money_transaction_successfully() throws Exception {
        var account = bankAccountHelper.havingOpened(anAccount().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final WithdrawalRequest withdrawalRequest =
                new WithdrawalRequest(300, account.accountNumberAsString(), "description");

        mockMvc.perform(post("/api/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(withdrawalRequest)))
                .andExpect(status().isOk());


        BankAccountEntity savedToAccount = bankAccountHelper.findByAccountNumber(account.accountNumber());

        assertThat(savedToAccount.getBalance()).isEqualTo(200);
    }
}
