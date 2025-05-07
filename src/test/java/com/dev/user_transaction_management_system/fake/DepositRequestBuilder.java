package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;

import java.time.LocalDateTime;

public class DepositRequestBuilder {
    private double amount = 1000;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String description = "transaction description!";
    private LocalDateTime createdAt =  LocalDateTime.of(2025, 5, 4, 14, 30, 0);

    public static DepositRequestBuilder aDepositRequest() {
        return new DepositRequestBuilder();
    }

    public DepositRequestBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public DepositRequestBuilder withFromAccount(Account fromAccount) {
        this.fromAccountNumber = fromAccount.accountNumberAsString();
        return this;
    }

    public DepositRequestBuilder withToAccount(Account toAccount) {
        this.toAccountNumber = toAccount.accountNumberAsString();
        return this;
    }

    public DepositRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DepositRequestBuilder withCreatedAt(LocalDateTime time) {
        this.createdAt = time;
        return this;
    }

    public DepositRequest initiate() {
        return new DepositRequest(amount, fromAccountNumber, toAccountNumber, description, createdAt);
    }

}
