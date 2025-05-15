package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WithdrawingMoneyTests extends BankAccountTestHelper {

    @InjectMocks
    private WithdrawingMoney withdrawingMoney;


    public WithdrawingMoneyTests() {
        super.accountRepository = new BankAccountRepositoryFake();
        withdrawingMoney = new WithdrawingMoney(new TransactionRepositoryFake(), super.accountRepository);
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
        final String description = "buy something!";
        final BankAccount bankAccount = havingOpened(anAccount().withBalance(balance));

        return new WithdrawalRequest(amount, bankAccount.accountNumberAsString(), description);
    }
}
