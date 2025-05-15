package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.transaction.TransactionStatus;
import com.dev.user_transaction_management_system.domain.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@Data
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Column(name = "from_account_number")
    private String fromAccountNumber;

    @Column(name = "to_account_number")
    private String toAccountNumber;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String description;
    private String referenceNumber;

    private TransactionEntity(String fromAccountNumber,
                              String toAccountNumber,
                              Double amount,
                              TransactionStatus transactionStatus,
                              LocalDateTime createdAt,
                              TransactionType transactionType,
                              String description,
                              String referenceNumber) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.createdAt = createdAt;
        this.transactionType = transactionType;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static TransactionEntity initOf(String fromAccountNumber,
                                           String toAccountNumber,
                                           double amount,
                                           TransactionStatus transactionStatus,
                                           TransactionType transactionType,
                                           String description,
                                           String referenceNumber,
                                           LocalDateTime createdAt
    ) {
        return new TransactionEntity(
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