package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.domain.transaction.TransactionStatus.PROCESSING;

public abstract class Transaction {

    protected final int transactionId;
    protected TransactionDetail transactionDetail;
    protected final TransactionStatus transactionStatus;
    protected final LocalDateTime createdAt;
    protected final String referenceNumber;


    Transaction(Integer transactionId,
                TransactionDetail transactionDetail,
                String referenceNumber,
                LocalDateTime createdAt) {

        this.transactionId = transactionId;
        this.transactionDetail = transactionDetail;
        this.createdAt = createdAt;
        this.transactionStatus = PROCESSING;
        this.referenceNumber = referenceNumber;
    }

    public abstract TransactionEntity toEntity();
}
