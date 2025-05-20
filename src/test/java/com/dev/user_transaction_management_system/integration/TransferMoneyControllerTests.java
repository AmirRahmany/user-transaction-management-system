package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.TransferMoneyRequest;
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
import java.util.UUID;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.TransferMoneyRequestBuilder.aTransferMoneyRequest;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
class TransferMoneyControllerTests extends BankAccountTestHelper {

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
    void init_transfer_money_transaction_successfully() throws Exception {
        final BankAccount from =havingOpened(anAccount().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final BankAccount to = havingOpened(anAccount().enabled().withAccountNumber("0300456574853")
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


        Optional<BankAccountEntity> savedToAccount = accountRepository.findByAccountNumber(to.accountNumber());
        Optional<BankAccountEntity> savedFromAccount = accountRepository.findByAccountNumber(from.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedFromAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.get().getBalance()).isEqualTo(200);
    }
}
