package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.time.LocalDateTime;

import static com.dev.user_transaction_management_system.domain.transaction.TransactionStatus.PROCESSING;
import static java.time.LocalDateTime.now;

public class Transaction {

    private final int transactionId;
    private final AccountNumber fromAccountNumber;
    private final AccountNumber toAccountNumber;
    private final Amount amount;
    private final String description;
    private final TransactionStatus transactionStatus;
    private final LocalDateTime createdAt;
    private final TransactionType transactionType;
    private final String referenceNumber;


    private Transaction(Integer transactionId,
                        AccountNumber fromAccountNumber,
                        AccountNumber toAccountNumber,
                        Amount amount,
                        TransactionType transactionType,
                        String description,
                        String referenceNumber,
                        LocalDateTime createdAt) {

        this.transactionId = transactionId;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
        this.createdAt = createdAt;
        this.transactionStatus = PROCESSING;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static Transaction of(
            Integer transactionId,
            AccountNumber fromAccountNumber,
            AccountNumber toAccountNumber,
            Amount amount,
            TransactionType transactionType,
            String description,
            String referenceNumber,
            LocalDateTime createdAt) {

        return new Transaction(transactionId,
                fromAccountNumber,
                toAccountNumber,
                amount,
                transactionType,
                description,
                referenceNumber,
                createdAt);
    }

    public TransactionEntity toEntity() {
        return TransactionEntity.initOf(
                fromAccountNumber.toString(),
                toAccountNumber.toString(),
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
                ", fromAccountId=" + fromAccountNumber +
                ", toAccountId=" + toAccountNumber +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", transactionStatus=" + transactionStatus +
                ", createdAt=" + createdAt +
                ", transactionType=" + transactionType +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }
}
