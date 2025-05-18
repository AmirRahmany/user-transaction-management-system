package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindBankAccount;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.fake.AccountFakeBuilder;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.use_case.dto.TransferMoneyRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.TransferMoneyRequestBuilder.aTransferMoneyRequest;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransferMoneyTests extends BankAccountTestHelper {

    private final TransferMoney transactionService;

    public TransferMoneyTests() {
        super.accountRepository = new BankAccountRepositoryFake();

        final TransactionRepositoryFake transactionRepository = new TransactionRepositoryFake();
        this.transactionService = new TransferMoney(transactionRepository, accountRepository);
    }


    @Test
    void initiate_transfer_money_transaction_successfully() {
        final BankAccount from = havingOpened(anAccount().enabled().withAccountNumber("0300145874512"));
        final BankAccount to = havingOpened(anAccount().enabled().withAccountNumber("0300100234111"));

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest()
                .withAmount(500)
                .withFromAccount(from)
                .withToAccount(to)
                .withDescription("transaction description")
                .initiate();

        assertThatNoException().isThrownBy(() -> transactionService.transfer(transferMoneyRequest));
    }

    @Test
    void can_not_initiate_a_transfer_money_request_with_unknown_account() {
        final BankAccount from = havingUnOpened(anAccount().withAccountNumber("0300145687654"));
        final BankAccount to = havingUnOpened(anAccount().withAccountNumber("0300874137630"));

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest().withAmount(300)
                .withFromAccount(from)
                .withToAccount(to)
                .initiate();

        assertThatExceptionOfType(CouldNotFindBankAccount.class)
                .isThrownBy(() -> transactionService.transfer(transferMoneyRequest));
    }

    @Test
    void can_not_initiate_a_transfer_money_request_with_negative_amount() {
        BankAccount from = havingOpened(anAccount().withAccountNumber("0300145241563"));
        BankAccount to = havingOpened(anAccount().withAccountNumber("0300874021436"));

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest()
                .withAmount(-100)
                .withFromAccount(from)
                .withToAccount(to)
                .initiate();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> transactionService.transfer(transferMoneyRequest));
    }

    @Test
    void cannot_transfer_money_from_account_with_insufficient_funds() {
        BankAccount insufficientAccount = havingOpened(anAccount().withAccountNumber("0300467800143").withBalance(200));
        BankAccount to = havingOpened(anAccount().withAccountNumber("0300465214701"));

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest()
                .withAmount(400)
                .withFromAccount(insufficientAccount)
                .withToAccount(to)
                .initiate();

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.transfer(transferMoneyRequest));
    }

    @Test
    void cannot_transfer_money_to_same_account() {
        BankAccount from = havingOpened(anAccount().withAccountNumber("0300450012365"));

        final TransferMoneyRequest transferMoneyRequest = aTransferMoneyRequest()
                .withFromAccount(from)
                .withToAccount(from)
                .initiate();

        assertThatExceptionOfType(CouldNotProcessTransaction.class)
                .isThrownBy(() -> transactionService.transfer(transferMoneyRequest));
    }



    private BankAccount havingUnOpened(AccountFakeBuilder accountBuilder) {
        return accountBuilder.open();
    }
}
