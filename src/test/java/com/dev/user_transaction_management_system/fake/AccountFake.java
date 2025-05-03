package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;

public class AccountFake {
    private String accountId = "32";
    private String accountNumber = "6210457865432109";
    private String userId = "user_987fbc97-4bed-5078-8f07-9141ba07c9f3";
    private Amount balance = Amount.of(5000.50);

    public static AccountFake account() {
        return new AccountFake();
    }

    public AccountFake withAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountFake withUserId(String userId) {
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
        return Account.open(accountId, accountNumber, userId, balance);
    }
}
