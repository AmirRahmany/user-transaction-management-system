package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.fake.BankAccountRepositoryFake;
import com.dev.user_transaction_management_system.fake.TransactionRepositoryFake;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.use_case.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;

class ViewTransactionHistoryTests extends BankAccountTestHelper {

    private TransferMoney transactionService;
    private ViewTransactionHistory viewTransactionHistory;

    @BeforeEach
    void setUp() {
        TransactionRepositoryFake transactionRepository = new TransactionRepositoryFake();
        super.accountRepository = new BankAccountRepositoryFake();
        this.transactionService = new TransferMoney(transactionRepository, accountRepository);
        this.viewTransactionHistory = new ViewTransactionHistory(transactionRepository);
    }

    @Test
    void show_user_transactions_successfully() {
        final BankAccount from = havingEnabledAccount();
        final BankAccount to = havingOpened(anAccount().enabled().withAccountNumber("0300450012325"));


        final TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest(100,
                from.accountNumberAsString(), to.accountNumberAsString(), "transaction");

        final TransferReceipt receipt = transactionService.transfer(transferMoneyRequest);

        final List<TransactionHistory> histories = viewTransactionHistory.getHistoryByAccountNumber(from.accountNumberAsString());

        TransactionHistory expectedHistory = new TransactionHistory("DEPOSIT", receipt.createdAt(),
                100,receipt.referenceNumber());
        assertThat(histories).containsOnly(expectedHistory);

    }
}
