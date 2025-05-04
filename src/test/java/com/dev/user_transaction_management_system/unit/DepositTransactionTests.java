package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.application.DepositTransaction;
import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import com.dev.user_transaction_management_system.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.model.AccountEntity;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import com.dev.user_transaction_management_system.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepositTransactionTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DepositTransaction transactionService;

    public DepositTransactionTests() {

    }

    @Test
    void initiate_deposit_transaction_successfully() {
        final Account from = mock(Account.class);
        Account to = mock(Account.class);

        AccountEntity fromEntity = mock(AccountEntity.class);
        AccountEntity toEntity = mock(AccountEntity.class);

        when(from.toEntity()).thenReturn(fromEntity);
        when(to.toEntity()).thenReturn(toEntity);

        final int value = 100;
        final Amount amount = Amount.of(value);


        when(from.accountId()).thenReturn(146);
        when(to.accountId()).thenReturn(22);

        Transaction expectedTransaction = Transaction.of(
                0 // TODO change it
                , 146,
                22,
                amount,
                TransactionType.DEPOSIT,
                "noting!",
                anyString(),
                getTime());


        assertThatNoException().isThrownBy(() -> {
            final Transaction transaction =
                    transactionService.deposit(amount, from, to, "noting!", getTime());
           // assertThat(transaction).isEqualTo(expectedTransaction);
        });

        verify(from).decreaseBalance(amount);
        verify(to).increaseAmount(amount);
        verify(accountRepository).save(from.toEntity());
        verify(accountRepository).save(to.toEntity());
        verify(transactionRepository).save(any());


    }

    @Test
    void can_not_initiate_a_deposit_transaction_with_negative_amount() {
        Account from = account().open();
        Account to = account().open();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionService.deposit(
                        Amount.of(-100), from, to, "noting!",getTime())
                );
    }

    @Test
    void cannot_deposit_from_account_with_insufficient_funds() {
        final Account from = account().withBalance(200).open();
        final Account to = account().open();
        final Amount amount = Amount.of(300);

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.deposit(amount, from, to, "noting!",getTime()));
    }

    @Test
    void cannot_deposit_to_same_account() {
        final Account from = account().open();
        final Account to = account().open();
        final Amount amount = Amount.of(300);

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.deposit(amount, from, to, "noting!",getTime()));
    }

    private static LocalDateTime getTime() {
        return LocalDateTime.of(2025, 5, 4, 14, 30, 0);
    }
}
