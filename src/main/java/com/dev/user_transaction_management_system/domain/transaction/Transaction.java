package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.model.TransactionEntity;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.domain.transaction.TransactionStatus.PROCESSING;
import static java.time.LocalDateTime.now;

public class Transaction {

    private final int transactionId;
    private final Integer fromAccountId;
    private final Integer toAccountId;
    private final Amount amount;
    private final String description;
    private final TransactionStatus transactionStatus;
    private final LocalDateTime createdAt;
    private final TransactionType transactionType;
    private final String referenceNumber;


    private Transaction(Integer transactionId, Integer fromAccountId,
                        Integer toAccountId,
                        Amount amount,
                        TransactionType transactionType,
                        String description,
                        String referenceNumber,
                        LocalDateTime createdAt) {

        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = createdAt;
        this.transactionStatus = PROCESSING;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static Transaction of(
            Integer transactionId,
            Integer fromAccountId,
            Integer toAccountId,
            Amount amount,
            TransactionType transactionType,
            String description,
            String referenceNumber,
            LocalDateTime createdAt) {

        return new Transaction(transactionId,
                fromAccountId,
                toAccountId,
                amount,
                transactionType,
                description,
                referenceNumber,
                createdAt);
    }

    public TransactionEntity toEntity() {
        return TransactionEntity.initOf(
                fromAccountId,
                toAccountId,
                amount.toValue(),
                transactionStatus,
                transactionType,
                description,
                referenceNumber,
                createdAt
        );
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", createdAt=" + createdAt +
                ", transactionType=" + transactionType +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
