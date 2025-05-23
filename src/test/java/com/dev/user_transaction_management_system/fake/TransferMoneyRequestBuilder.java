package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.use_case.dto.TransferMoneyRequest;

public class TransferMoneyRequestBuilder {
    private double amount = 1000;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String description = "transaction description!";

    public static TransferMoneyRequestBuilder aTransferMoneyRequest() {
        return new TransferMoneyRequestBuilder();
    }

    public TransferMoneyRequestBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TransferMoneyRequestBuilder withFromAccount(BankAccount fromAccount) {
        this.fromAccountNumber = fromAccount.accountNumberAsString();
        return this;
    }

    public TransferMoneyRequestBuilder withToAccount(BankAccount toAccount) {
        this.toAccountNumber = toAccount.accountNumberAsString();
        return this;
    }

    public TransferMoneyRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransferMoneyRequest initiate() {
        return new TransferMoneyRequest(amount, fromAccountNumber, toAccountNumber, description);
    }

}
