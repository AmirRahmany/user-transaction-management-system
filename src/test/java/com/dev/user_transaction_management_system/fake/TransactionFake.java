package com.dev.user_transaction_management_system.fake;

import com.dev.user_transaction_management_system.domain.transaction.*;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.fake.AccountFake.account;

public class TransactionFake {
    private int transactionId;
    private Integer fromAccountId = account().open().accountId();
    private Integer toAccountId = account().open().accountId();
    private String description;
    private String referenceNumber;
    private TransactionStatus transactionStatus = TransactionStatus.PROCESSING;
    private Amount amount = Amount.of(200);
    private TransactionType transactionType;


    public static TransactionFake transaction() {
        return new TransactionFake();
    }

    public TransactionFake withTransactionId(int transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public TransactionFake withFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
        return this;
    }

    public TransactionFake withToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
        return this;
    }

    public TransactionFake withStatus(TransactionStatus status) {
        this.transactionStatus = status;
        return this;
    }

    public TransactionFake withDescription(String description) {
        this.description = description;
        return this;
    }

    public TransactionFake withReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    public TransactionFake withAmount(double amount) {
        this.amount = Amount.of(amount);
        return this;
    }

    public TransactionFake withTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public TransactionFake withInsufficientBalance() {
        this.fromAccountId = AccountFake.account().withInsufficientBalance().open().accountId();
        return this;
    }

    public Transaction initiate() {
        return Transaction.of(0, fromAccountId, toAccountId, amount, transactionType, description, referenceNumber, getTime());
    }

    public LocalDateTime getTime(){
        return LocalDateTime.of(2025, 5, 4, 14, 30, 0);
    }

}
