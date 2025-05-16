package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;

public class DepositRequestBuilder {
    private double amount = 1000;
    private String accountNumber;
    private String description = "transaction description!";

    public static DepositRequestBuilder aDepositRequest() {
        return new DepositRequestBuilder();
    }

    public DepositRequestBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public DepositRequestBuilder withAccount(BankAccount fromAccount) {
        this.accountNumber = fromAccount.accountNumberAsString();
        return this;
    }

    public DepositRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DepositRequest initiate() {
        return new DepositRequest(amount, accountNumber, description);
    }

}
