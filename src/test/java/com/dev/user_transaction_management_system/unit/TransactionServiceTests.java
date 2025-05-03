package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.application.TransactionService;
import com.dev.user_transaction_management_system.application.TransactionServiceimpl;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.exceptions.InsufficientAccountBalance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;
import static com.dev.user_transaction_management_system.fake.TransactionFake.transaction;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTests {

    private final TransactionService transactionService;

    public TransactionServiceTests() {
        transactionService = new TransactionServiceimpl();
    }

    @Test
    void initiate_deposit_transaction_successfully() {
        final Transaction transaction = transaction().initiate();

        Account to = account().open();
        assertThatNoException().isThrownBy(() -> transactionService.deposit(transaction, to));
    }

    @Test
    void can_not_record_a_deposit_transaction_with_negative_amount() {
        Account to = account().open();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    final Transaction transaction = transaction().withAmount(-100).initiate();
                    transactionService.deposit(transaction, to);
                });
    }

    @Test
    void cant_initiate_deposit_to_another_account_without_enough_balance() {
        final Transaction transaction = transaction().withInsufficientBalance().initiate();
        Account to = account().open();

        assertThatExceptionOfType(InsufficientAccountBalance.class)
                .isThrownBy(() -> transactionService.deposit(transaction, to));
    }
}
