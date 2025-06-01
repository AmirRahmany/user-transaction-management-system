package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.transfer_money.TransferMoneyRequest;
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

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;
import static com.dev.user_transaction_management_system.test_builder.TransferMoneyRequestTestBuilder.aTransferMoneyRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
class TransferMoneyControllerTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @Autowired
    private BankAccountTestHelper bankAccountHelper;

    private User userAccount;
    private String token;

    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.givenActivatedSignedInUserWithToken();
        token = userAndToken.token();
        userAccount = userAndToken.user();
    }

    @Test
    void init_transfer_money_transaction_successfully() throws Exception {
        final BankAccount from = bankAccountHelper.havingOpened(anAccount().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final BankAccount to = bankAccountHelper.havingOpened(anAccount().enabled().withAccountNumber("0300456574853")
                .withBalance(140)
                .withUser(userAccount)); //TODO create different fake users

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest()
                .withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .initiate();

        mockMvc.perform(post("/api/transaction/transfer")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",token)
                        .content(objectMapper.writeValueAsString(transferMoneyRequest)))
                .andExpect(status().isOk());


        BankAccountEntity savedToAccount = bankAccountHelper.findByAccountNumber(to.accountNumber());
        BankAccountEntity savedFromAccount = bankAccountHelper.findByAccountNumber(from.accountNumber());

        assertThat(savedToAccount.getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.getBalance()).isEqualTo(200);
    }
}
