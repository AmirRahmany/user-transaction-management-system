package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.DepositRequestBuilder.aDepositRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void deposit_transaction_successfully() throws Exception {
        final BankAccount from =havingOpened(anAccount().withAccountNumber("0300654789123").withBalance(500));
        final BankAccount to = havingOpened(anAccount().withAccountNumber("0300456574853")
                .withAccountId(321)
                .withBalance(140)
                .withUserId(31));

        final LocalDateTime createdAt = LocalDateTime.of(2025, 5, 4, 14, 30, 0);

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .withCreatedAt(createdAt).initiate();

        mockMvc.perform(post("/api/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk());


        Optional<AccountEntity> savedToAccount = accountRepository.findByAccountNumber(to.accountNumber());
        Optional<AccountEntity> savedFromAccount = accountRepository.findByAccountNumber(from.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedFromAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.get().getBalance()).isEqualTo(200);
    }
}
