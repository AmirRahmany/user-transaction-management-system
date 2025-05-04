package com.dev.user_transaction_management_system.model;

import com.dev.user_transaction_management_system.domain.transaction.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;
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
    private int transactionId;
    private Integer fromAccountId;
    private Integer toAccountId;
    private Double amount;
    private TransactionStatus transactionStatus;
    private LocalDateTime createdAt;
    private TransactionType transactionType;
    private String description;
    private String referenceNumber;

    private TransactionEntity(int transactionId,
                              Integer fromAccountId,
                              Integer toAccountId,
                              Double amount,
                              TransactionStatus transactionStatus,
                              LocalDateTime createdAt,
                              TransactionType transactionType,
                              String description,
                              String referenceNumber) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.createdAt = createdAt;
        this.transactionType = transactionType;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static TransactionEntity
    initOf(Integer fromAccountId,
           Integer toAccountId,
           double amount,
           TransactionStatus transactionStatus,
           TransactionType transactionType,
           String description,
           String referenceNumber,
           LocalDateTime createdAt
    ) {
        return new TransactionEntity(0,
                fromAccountId,
                toAccountId,
                amount,
                transactionStatus,
                createdAt,
                transactionType,
                description,
                referenceNumber);

    }
}