package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.domain.transaction.TransactionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class WithdrawalTransaction {

    private final TransactionRepository transactionRepository;

    public WithdrawalTransaction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction withdraw(Account account, Amount amount, String description) {
        if (account.isBalanceInsufficient(amount))
            throw CouldNotProcessTransaction.becauseInsufficientBalance();

        String referenceNumber = UUID.randomUUID().toString();
        final Transaction transaction = Transaction.of(
                0, account.accountId(),
                account.accountId(),
                amount, TransactionType.WITHDRAWAL, description, referenceNumber, LocalDateTime.now());

        transactionRepository.save(transaction.toEntity());
        return transaction;
    }
}
