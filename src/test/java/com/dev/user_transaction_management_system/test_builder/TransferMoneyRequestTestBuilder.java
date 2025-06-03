package com.dev.user_transaction_management_system.test_builder;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.use_case.transfer_money.TransferMoneyRequest;

public class TransferMoneyRequestTestBuilder {
    private double amount = 1000;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String description = "transaction description!";

    public static TransferMoneyRequestTestBuilder aTransferMoneyRequest() {
        return new TransferMoneyRequestTestBuilder();
    }

    public TransferMoneyRequestTestBuilder withAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TransferMoneyRequestTestBuilder withFromAccount(BankAccount fromAccount) {
        this.fromAccountNumber = fromAccount.accountNumberAsString();
        return this;
    }

    public TransferMoneyRequestTestBuilder withToAccount(BankAccount toAccount) {
        this.toAccountNumber = toAccount.accountNumberAsString();
        return this;
    }

    public TransferMoneyRequestTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransferMoneyRequest initiate() {
        return new TransferMoneyRequest(amount, fromAccountNumber, toAccountNumber, description);
    }

}
