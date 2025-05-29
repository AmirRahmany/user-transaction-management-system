package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.fake.FakeClock;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.BankAccountMapper;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import org.junit.jupiter.api.Test;

import static com.dev.user_transaction_management_system.test_builder.BankAccountTestBuilder.anAccount;
import static com.dev.user_transaction_management_system.test_builder.DepositRequestTestBuilder.aDepositRequest;
import static org.assertj.core.api.Assertions.*;

class DepositingMoneyTests  {

    private final BankAccountTestHelper bankAccountTestHelper;
    private final DepositingMoney depositingMoney;

    public DepositingMoneyTests() {
        final BankAccountRepositoryFake accountRepository = new BankAccountRepositoryFake();

        this.depositingMoney = new DepositingMoney(new TransactionRepositoryFake(),
                accountRepository,
                new FakeEventPublisher(),
                new BankAccountMapper(),
                new FakeClock());
        this.bankAccountTestHelper = new BankAccountTestHelper(accountRepository);
    }

    @Test
    void deposit_money_successfully() {
        var bankAccount = bankAccountTestHelper.havingOpened(anAccount().enabled().withBalance(500));

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(300)
                .withAccountNumber(bankAccount.accountNumberAsString()).initiate();


        assertThatNoException().isThrownBy(()-> {
            final TransactionReceipt transactionReceipt = depositingMoney.deposit(depositRequest);
            assertThat(transactionReceipt).isNotNull();
            assertThat(transactionReceipt.accountNumber()).isEqualTo(bankAccount.accountNumberAsString());
            assertThat(transactionReceipt.referenceNumber()).isEqualTo("03004565879851");
        });
    }

    @Test
    void cannot_deposit_money_with_negative_amount() {
        var bankAccount = bankAccountTestHelper.havingOpened(anAccount().enabled().withBalance(500));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(()->{
            var depositRequest = aDepositRequest().withAmount(-200)
                    .withAccountNumber(bankAccount.accountNumberAsString()).initiate();

            depositingMoney.deposit(depositRequest);
        });
    }

    @Test
    void can_not_deposit_money_to_non_existed_bank_account() {
        String nonRegisteredAccountNumber = "0300456578451";
        final DepositRequest depositRequest = aDepositRequest()
                .withAccountNumber(nonRegisteredAccountNumber).initiate();

        assertThatExceptionOfType(CouldNotFindBankAccount.class).isThrownBy(()->depositingMoney
                .deposit(depositRequest));
    }

    @Test
    void cannot_deposit_money_to_disable_bank_account() {
        var disableAccount = bankAccountTestHelper.havingOpened(anAccount().disabled());

        final DepositRequest depositRequest = aDepositRequest()
                .withAccountNumber(disableAccount.accountNumberAsString()).initiate();

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(()->depositingMoney.deposit(depositRequest));
    }
}
