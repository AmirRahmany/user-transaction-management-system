package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.user.User;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.domain.transaction.TransactionStatus.PROCESSING;
import static java.time.LocalDateTime.now;

public class Transaction {

    private final int transactionId;
    private final String userId;
    private final Account account;
    private final TransactionStatus transactionStatus;
    private final LocalDateTime createdAt;
    private final Amount amount;
    private final TransactionType transactionType;


    private Transaction(String userId,
                        Account from,
                        Amount amount,
                        TransactionType transactionType) {

        this.transactionId = 0;
        this.userId = userId;
        this.account = from;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = now();
        this.transactionStatus = PROCESSING;
    }

    public static Transaction of(String userId,
                                 Account account,
                                 Amount amount,
                                 TransactionType transactionType) {

        return new Transaction(userId, account, amount, transactionType);
    }

    public boolean isBalanceSufficient() {
        return account.hasEnoughBalance(amount);
    }

    public boolean isBalanceInsufficient() {
        return !isBalanceSufficient();
    }

}
