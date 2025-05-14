package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;

import java.util.UUID;

public class AccountRequestFakeBuilder {
    public static final String NO_USER = UUID.randomUUID().toString();
    private String userId = UUID.randomUUID().toString();
    private double balance = 500;

    private AccountRequestFakeBuilder() {

    }

    public static AccountRequestFakeBuilder accountRequest() {
        return new AccountRequestFakeBuilder();
    }

    public AccountRequestFakeBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AccountRequestFakeBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public AccountRequestFakeBuilder withNoUser() {
        this.userId = NO_USER;
        return this;
    }

    public AccountRequest open() {
        return new AccountRequest(userId, balance);
    }
}
