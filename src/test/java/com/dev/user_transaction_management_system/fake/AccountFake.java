package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;

import java.time.LocalDateTime;

public class AccountFake {
    private Integer accountId = 32;
    private String accountNumber = "6210457865432109";
    private Integer userId = 45;
    private Amount balance = Amount.of(5000.50);

    public static AccountFake account() {
        return new AccountFake();
    }

    public AccountFake withAccountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountFake withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public AccountFake withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public AccountFake withBalance(double balance) {
        this.balance = Amount.of(balance);
        return this;
    }

    public AccountFake withInsufficientBalance() {
        this.balance = Amount.of(0);
        return this;
    }

    public Account open() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 6, 14, 8, 16, 15);
        return Account.open(accountId, AccountNumber.of(accountNumber), userId, balance, createdAt);
    }

    public AccountFake withNoUser() {
        userId = null;
        return this;
    }
}
