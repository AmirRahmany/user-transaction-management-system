package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;

public class AccountRequestFakeBuilder {
    private String email = aUser().build().email().asString();
    private double balance = 500;

    private AccountRequestFakeBuilder() {

    }

    public static AccountRequestFakeBuilder accountRequest() {
        return new AccountRequestFakeBuilder();
    }

    public AccountRequestFakeBuilder withUsername(String userId) {
        this.email = userId;
        return this;
    }

    public AccountRequestFakeBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public AccountRequestFakeBuilder withNoUser() {
        this.email = FakeUser.UNKNOWN_USER;
        return this;
    }

    public AccountRequest open() {
        return new AccountRequest(email, balance);
    }
}
