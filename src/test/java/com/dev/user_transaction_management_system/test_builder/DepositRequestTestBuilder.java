package com.dev.user_transaction_management_system.test_builder;

import com.dev.user_transaction_management_system.use_case.deposit_money.DepositRequest;

public class DepositRequestTestBuilder {
    private double amount = 1000;
    private String accountNumber;
    private String description = "transaction description!";

    public static DepositRequestTestBuilder aDepositRequest() {
        return new DepositRequestTestBuilder();
    }

    public DepositRequestTestBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public DepositRequestTestBuilder withAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public DepositRequestTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DepositRequest initiate() {
        return new DepositRequest(amount, accountNumber, description);
    }

}
