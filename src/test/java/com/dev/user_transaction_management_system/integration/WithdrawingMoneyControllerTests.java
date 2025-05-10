package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.account.BankAccount;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.fake.AccountFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class WithdrawingMoneyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void withdraw_money_transaction_successfully() throws Exception {

        final BankAccount account = havingOpened(anAccount().withAccountNumber("0300654789123").withBalance(500));

        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest(300, account.accountNumberAsString(), "description");

        mockMvc.perform(post("/api/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(withdrawalRequest)))
                .andExpect(status().isOk());


        Optional<AccountEntity> savedToAccount = accountRepository.findByAccountNumber(account.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(200);
    }

    private BankAccount havingOpened(AccountFakeBuilder accountFakeBuilder) {
        final BankAccount account = accountFakeBuilder.open();
        accountRepository.save(account.toEntity());
        return account;
    }
}
