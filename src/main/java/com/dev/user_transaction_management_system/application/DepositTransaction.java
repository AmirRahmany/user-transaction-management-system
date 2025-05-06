package com.dev.user_transaction_management_system.application;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import com.dev.user_transaction_management_system.exceptions.CouldNotProcessTransaction;
import com.dev.user_transaction_management_system.repository.AccountRepository;
import com.dev.user_transaction_management_system.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DepositTransaction {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public DepositTransaction(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction deposit(Amount amount, Account from, Account to, String description, LocalDateTime createdAt) {
        if (from.isTheSameAccountWith(to)) throw CouldNotProcessTransaction.becauseSourceAndTargetAccountsAreTheSame();
        validateBalanceSufficient(amount, from);

        from.decreaseBalance(amount);
        to.increaseAmount(amount);

        String referenceNumber = UUID.randomUUID().toString();
        final Transaction transaction = Transaction.of(
                0,
                from.accountId(),
                to.accountId(),
                amount,
                TransactionType.DEPOSIT,
                description,
                referenceNumber,
                createdAt);



        accountRepository.save(from.toEntity());
        accountRepository.save(to.toEntity());
        transactionRepository.save(transaction.toEntity());

        // return dto for user
        return transaction;
    }

    private static void validateBalanceSufficient(Amount amount, Account from) {
        if (from.isBalanceInsufficient(amount)) {
            throw CouldNotProcessTransaction.becauseInsufficientBalance();
        }
    }
}
