package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.AccountId;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
import com.dev.user_transaction_management_system.domain.user.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

public class AccountFakeBuilder {
    private Integer accountId = 32;
    private String accountNumber = "0300123002145";
    private UUID userId = UUID.randomUUID();
    private Amount balance = Amount.of(5000.50);

    public static AccountFakeBuilder anAccount() {
        return new AccountFakeBuilder();
    }

    public AccountFakeBuilder withAccountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountFakeBuilder withUserId(String userId) {
        this.userId = UUID.fromString(userId);
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
        final AccountId id = AccountId.fromInt(accountId);
        final UUID uuid = UUID.randomUUID();
        return BankAccount.open(id, AccountNumber.of(accountNumber), UserId.fromUUID(uuid), balance, createdAt);
    }
}
