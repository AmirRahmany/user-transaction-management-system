package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.application.DepositTransaction;
import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.model.AccountEntity;
import com.dev.user_transaction_management_system.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DepositTransactionIntegrationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DepositTransaction depositTransaction;

    @Test
    void deposit_transaction_successfully() {

        final Account from = account().withBalance(500).open();
        final Account to = account().withAccountId(321).withBalance(140).withUserId(31).open();
        final LocalDateTime createdAt = LocalDateTime.of(2025, 5, 4, 14, 30, 0);
        final Amount amount = Amount.of(300);
        entityManager.merge(from.toEntity());
        entityManager.merge(to.toEntity());

        assertThatNoException().isThrownBy(() ->
                depositTransaction.deposit(amount, from, to, "noting!",createdAt));

        Optional<AccountEntity> savedToAccount = transactionRepository.findAccountById(to.accountId());
        Optional<AccountEntity> savedFromAccount = transactionRepository.findAccountById(from.accountId());

        assertThat(savedToAccount).isPresent();
        assertThat(savedFromAccount).isPresent();
        assertThat(savedToAccount.get().getBalance()).isEqualTo(440);
        assertThat(savedFromAccount.get().getBalance()).isEqualTo(200);
    }


}
