package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.account.AccountRepository;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.AccountFakeBuilder;
import com.dev.user_transaction_management_system.fake.AccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.DepositRequestBuilder.aDepositRequest;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepositingMoneyTests {

    private final AccountRepository accountRepository;

    private final DepositingMoney transactionService;

    public DepositingMoneyTests() {
        this.accountRepository = new AccountRepositoryFake();
        this.transactionService = new DepositingMoney(new TransactionRepositoryFake(), accountRepository);
    }


    @Test
    void initiate_deposit_transaction_successfully() {
        final Account from = havingOpened(anAccount().withAccountNumber("0300145874512"));
        final Account to = havingOpened(anAccount().withAccountNumber("0300100234111"));

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(500)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .initiate();

        assertThatNoException().isThrownBy(() -> transactionService.deposit(depositRequest));
    }

    @Test
    void can_not_initiate_a_deposit_transaction_with_unknown_account() {
        final Account from = havingUnOpened(anAccount().withAccountNumber("0300145687654"));
        final Account to = havingUnOpened(anAccount().withAccountNumber("0300874137630"));

        final DepositRequest depositRequest = aDepositRequest().withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .withCreatedAt(now())
                .initiate();

        assertThatExceptionOfType(CouldNotFindAccount.class)
                .isThrownBy(() -> transactionService.deposit(depositRequest));
    }

    private Account havingUnOpened(AccountFakeBuilder accountBuilder) {
        return accountBuilder.open();
    }

    @Test
    void can_not_initiate_a_deposit_transaction_with_negative_amount() {
        Account from = havingOpened(anAccount().withAccountNumber("0300145241563"));
        Account to = havingOpened(anAccount().withAccountNumber("0300874021436"));

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(-100)
                .withFromAccount(from)
                .withToAccount(to)
                .initiate();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionService.deposit(depositRequest));
    }

    @Test
    void cannot_deposit_from_account_with_insufficient_funds() {
        Account insufficientAccount = havingOpened(anAccount().withAccountNumber("0300467800143").withBalance(200));
        Account to = havingOpened(anAccount().withAccountNumber("0300465214701"));

        final DepositRequest depositRequest = aDepositRequest()
                .withAmount(400)
                .withFromAccount(insufficientAccount)
                .withToAccount(to)
                .initiate();

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.deposit(depositRequest));
    }

    @Test
    void cannot_deposit_to_same_account() {
        Account from = havingOpened(anAccount().withAccountNumber("0300450012365"));

        final DepositRequest depositRequest = aDepositRequest()
                .withFromAccount(from)
                .withToAccount(from)
                .initiate();

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.deposit(depositRequest));
    }


    private Account havingOpened(AccountFakeBuilder accountFakeBuilder) {
        final Account account = accountFakeBuilder.open();
        accountRepository.save(account.toEntity());
        return account;
    }
}
