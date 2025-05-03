package com.dev.user_transaction_management_system.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private String accountId;
    private String accountNumber;
    private String userId;
    private Amount balance;
    private LocalDateTime createdAt;
    private int status;

    private Account(String accountId, String accountNumber, String userId, Amount balance) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
        this.status = 1;
    }

    public static Account open(String accountId, String accountNumber, String userId, Amount balance) {
        return new Account(accountId, accountNumber, userId, balance);
    }

    public boolean hasEnoughBalance(Amount amount) {
        return balance.toValue() >= amount.toValue();
    }
}
