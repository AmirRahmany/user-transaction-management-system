package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.domain.transaction.TransactionStatus.PROCESSING;

public abstract class Transaction {

    protected final TransactionId transactionId;
    protected TransactionDetail transactionDetail;
    protected final TransactionStatus transactionStatus;
    protected final LocalDateTime createdAt;
    protected final ReferenceNumber referenceNumber;


    Transaction(TransactionId transactionId,
                TransactionDetail transactionDetail,
                ReferenceNumber referenceNumber,
                LocalDateTime createdAt) {
        Assert.notNull(transactionId,"transaction id cannot be null");
        Assert.notNull(transactionDetail,"transaction detail cannot be null");
        Assert.notNull(referenceNumber,"reference number cannot be null");
        Assert.notNull(createdAt,"created at cannot be null");

        this.transactionId = transactionId;
        this.transactionDetail = transactionDetail;
        this.createdAt = createdAt;
        this.transactionStatus = PROCESSING;
        this.referenceNumber = referenceNumber;
    }

    public abstract TransactionEntity toEntity();
}
