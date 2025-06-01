package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.fake.FakeClock;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.withdraw_money.WithdrawalRequest;
import com.dev.user_transaction_management_system.use_case.withdraw_money.WithdrawMoney;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WithdrawMoneyTests {

    private final BankAccountTestHelper helper;


    private final WithdrawMoney withdrawMoney;


    public WithdrawMoneyTests() {
        final BankAccountRepositoryFake accountRepositoryFake = new BankAccountRepositoryFake();
        withdrawMoney = new WithdrawMoney(new TransactionRepositoryFake(),
                accountRepositoryFake,
                new FakeEventPublisher(),
                new BankAccountMapper(),
                new FakeClock());
        this.helper = new BankAccountTestHelper(accountRepositoryFake);
    }

    @Test
    void init_withdrawal_transaction_successfully() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(200D, 500);

        assertThatNoException().isThrownBy(() -> withdrawMoney.withdraw(withdrawalRequest));
    }

    @Test
    void cant_init_withdrawal_transaction_with_insufficient_balance() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(600, 500);

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> withdrawMoney.withdraw(withdrawalRequest));
    }

    @Test
    void cant_init_withdrawal_transaction_with_negative_amount() {
        final WithdrawalRequest withdrawalRequest = withdrawalRequestOf(-500d, 300);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> withdrawMoney.withdraw(withdrawalRequest));
    }

    private WithdrawalRequest withdrawalRequestOf(double amount, double balance) {
        final String description = "buy something!";
        final BankAccount bankAccount = helper.havingOpened(anAccount().withBalance(balance));

        return new WithdrawalRequest(amount, bankAccount.accountNumberAsString(), description);
    }
}
