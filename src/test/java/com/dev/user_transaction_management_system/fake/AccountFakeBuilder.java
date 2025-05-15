package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountId;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountFakeBuilder {
    private UUID accountId = UUID.randomUUID();
    private String accountNumber = "0300123002145";
    private String userId = "8c5148ea-857b-4996-a09c-5a5131a33564";
    private Amount balance = Amount.of(5000.50);

    public static AccountFakeBuilder anAccount() {
        return new AccountFakeBuilder();
    }

    public AccountFakeBuilder withAccountId(String accountId) {
        this.accountId = UUID.fromString(accountId);
        return this;
    }

    public AccountFakeBuilder withUserId(String userId) {
        this.userId = UUID.fromString(userId).toString();
        return this;
    }

    public AccountFakeBuilder withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public AccountFakeBuilder withBalance(double balance) {
        this.balance = Amount.of(balance);
        return this;
    }

    public AccountFakeBuilder withInsufficientBalance() {
        this.balance = Amount.of(200);
        return this;
    }

    public BankAccount open() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 6, 14, 8, 16, 15);
        final AccountId id = AccountId.fromUUID(UUID.fromString(accountId.toString()));
        return BankAccount.open(id, AccountNumber.of(accountNumber), UserId.fromString(userId), balance, createdAt);
    }
}
