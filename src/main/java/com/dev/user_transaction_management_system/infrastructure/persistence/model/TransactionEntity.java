package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.transaction.TransactionStatus;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@Data
public class TransactionEntity {

    @Id
    private Integer transactionId;
    private String fromAccountNumber;
    private String toAccountNumber;
    private Double amount;
    private TransactionStatus transactionStatus;
    private LocalDateTime createdAt;
    private TransactionType transactionType;
    private String description;
    private String referenceNumber;

    private TransactionEntity(int transactionId,
                              String fromAccountNumber,
                              String toAccountNumber,
                              Double amount,
                              TransactionStatus transactionStatus,
                              LocalDateTime createdAt,
                              TransactionType transactionType,
                              String description,
                              String referenceNumber) {
        this.transactionId = transactionId;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.createdAt = createdAt;
        this.transactionType = transactionType;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static TransactionEntity
    initOf(String fromAccountNumber,
           String toAccountNumber,
           double amount,
           TransactionStatus transactionStatus,
           TransactionType transactionType,
           String description,
           String referenceNumber,
           LocalDateTime createdAt
    ) {
        return new TransactionEntity(0,
                fromAccountNumber,
                toAccountNumber,
                amount,
                transactionStatus,
                createdAt,
                transactionType,
                description,
                referenceNumber);

    }
}