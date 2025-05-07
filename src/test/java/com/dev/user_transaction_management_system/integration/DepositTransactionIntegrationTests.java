package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.use_case.DepositTransaction;
import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.AccountEntity;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.DepositRequestBuilder.aDepositRequest;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DepositTransactionIntegrationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepositTransaction depositTransaction;

    @Test
    void deposit_transaction_successfully() {

        final Account from = anAccount().withAccountNumber("0300654789123")
                .withBalance(500).open();

        final Account to = anAccount().withAccountNumber("0300456574853")
                .withAccountId(321)
                .withBalance(140)
                .withUserId(31).open();

        final LocalDateTime createdAt = LocalDateTime.of(2025, 5, 4, 14, 30, 0);
        accountRepository.save(from.toEntity());
        accountRepository.save(to.toEntity());

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .withCreatedAt(createdAt).initiate();

        assertThatNoException().isThrownBy(() -> depositTransaction.deposit(depositRequest));

        Optional<AccountEntity> savedToAccount = accountRepository.findByAccountNumber(to.accountNumber());
        Optional<AccountEntity> savedFromAccount = accountRepository.findByAccountNumber(from.accountNumber());

        assertThat(savedToAccount).isPresent();
        assertThat(savedFromAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.get().getBalance()).isEqualTo(200);
    }
}
