package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class Transaction {

    private final TransactionId transactionId;
    private TransactionDetail transactionDetail;
    private final AccountNumber fromAccountNumber;
    private final AccountNumber toAccountNumber;
    private final LocalDateTime createdAt;
    private final ReferenceNumber referenceNumber;

    public Transaction(
            TransactionId transactionId,
            AccountNumber fromAccountNumber,
            AccountNumber toAccountNumber,
            TransactionDetail transactionDetail,
            LocalDateTime createdAt,
            ReferenceNumber referenceNumber) {

        Assert.notNull(transactionId, "transaction id cannot be null");
        Assert.notNull(transactionDetail, "transaction detail cannot be null");
        Assert.notNull(referenceNumber, "reference number cannot be null");
        Assert.notNull(createdAt, "created at cannot be null");

        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.createdAt = createdAt;
        this.referenceNumber = referenceNumber;
        this.transactionDetail = transactionDetail;
        this.transactionId = transactionId;
    }

    public static Transaction of(
            TransactionId transactionId,
            AccountNumber fromAccountNumber,
            AccountNumber toAccountNumber,
            TransactionDetail transactionDetail,
            ReferenceNumber referenceNumber,
            LocalDateTime createdAt) {

        return new Transaction(transactionId,
                fromAccountNumber,
                toAccountNumber,
                transactionDetail,
                createdAt,
                referenceNumber);
    }


    public TransactionEntity toEntity() {
        final TransactionEntity entity = new TransactionEntity();
        entity.setAmount(transactionDetail.amount().asDouble());
        entity.setDescription(transactionDetail.description());
        entity.setTransactionType(transactionDetail.transactionType());
        entity.setFromAccountNumber(fromAccountNumber.toString());
        if (toAccountNumber != null)
            entity.setToAccountNumber(toAccountNumber.toString());

        entity.setReferenceNumber(referenceNumber.toString());
        entity.setCreatedAt(createdAt);
        return entity;
    }
}
