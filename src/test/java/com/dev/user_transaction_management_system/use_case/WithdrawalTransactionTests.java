package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawalTransactionTests {


    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawalTransaction withdrawalTransaction;


    @Test
    @Disabled
    void init_withdrawal_transaction_successfully() {
        final Account account = anAccount().withBalance(500).open();
        final Amount amount = Amount.of(200);
        final String description = "buy something!";

        doNothing().when(transactionRepository).save(any(TransactionEntity.class));
        assertThatNoException().isThrownBy(() -> withdrawalTransaction.withdraw(account, amount, description));
        verify(transactionRepository).save(any());
    }

    @Test
    void cant_init_withdrawal_transaction_with_insufficient_balance() {
        final Account account = anAccount().withBalance(500).open();
        final Amount amount = Amount.of(600);
        final String description = "buy something!";

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> withdrawalTransaction.withdraw(account, amount, description));
    }

    @Test
    void cant_init_withdrawal_transaction_with_negative_amount() {
        final Account account = anAccount().withBalance(500).open();
        final String description = "buy something!";

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> withdrawalTransaction.withdraw(account, Amount.of(-500), description));
    }
}
