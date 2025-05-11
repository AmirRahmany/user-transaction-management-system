package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;

public class AccountRequestFakeBuilder {
    public static final Integer NO_USER = null;
    private Integer userId = 45;
    private double balance = 500;

    private AccountRequestFakeBuilder() {

    }

    public static AccountRequestFakeBuilder accountRequest() {
        return new AccountRequestFakeBuilder();
    }

    public AccountRequestFakeBuilder withUserId(Integer userId) {
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
