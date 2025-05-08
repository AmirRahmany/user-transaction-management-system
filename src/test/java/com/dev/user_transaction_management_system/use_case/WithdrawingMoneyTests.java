package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.AccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
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
class WithdrawingMoneyTests {


    private final AccountRepositoryFake accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawingMoney withdrawingMoney;


    public WithdrawingMoneyTests() {
        accountRepository = new AccountRepositoryFake();
        withdrawingMoney = new WithdrawingMoney(new TransactionRepositoryFake(), accountRepository);
    }

    @Test
    void init_withdrawal_transaction_successfully() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(200D, 500);

        assertThatNoException().isThrownBy(() -> withdrawingMoney.withdraw(withdrawalRequest));
    }

    @Test
    void cant_init_withdrawal_transaction_with_insufficient_balance() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(600, 500);

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> withdrawingMoney.withdraw(withdrawalRequest));
    }

    @Test
    void cant_init_withdrawal_transaction_with_negative_amount() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(-500d, 300);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> withdrawingMoney.withdraw(withdrawalRequest));
    }

    private WithdrawalRequest withdrawalRequestOf(double amount, double balance) {
        final Account account = anAccount().withBalance(balance).open();
        final String description = "buy something!";

        accountRepository.save(account.toEntity());

        return new WithdrawalRequest(amount, account.accountNumberAsString(), description);
    }
}
