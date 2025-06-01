package com.dev.user_transaction_management_system.test_builder;

import com.dev.user_transaction_management_system.use_case.dto.AccountRequest;

import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;

public class AccountRequestTestBuilder {
    private String email = aUser().build().email().asString();
    private double balance = 500;

    private AccountRequestTestBuilder() {

    }

    public static AccountRequestTestBuilder accountRequest() {
        return new AccountRequestTestBuilder();
    }

    public AccountRequestTestBuilder withUsername(String userId) {
        this.email = userId;
        return this;
    }

    public AccountRequestTestBuilder withBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public AccountRequestTestBuilder withNoUser() {
        this.email = FakeUser.UNKNOWN_USER;
        return this;
    }

    public AccountRequest open() {
        return new AccountRequest(email, balance);
    }
}
