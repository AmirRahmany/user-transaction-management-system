package com.dev.user_transaction_management_system.infrastructure.persistence.model;

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

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String description;
    private String referenceNumber;

    private TransactionEntity(String accountNumber,
                              Double amount,
                              LocalDateTime createdAt,
                              TransactionType transactionType,
                              String description,
                              String referenceNumber) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.createdAt = createdAt;
        this.transactionType = transactionType;
        this.description = description;
        this.referenceNumber = referenceNumber;
    }

    public static TransactionEntity initOf(String accountNumber,
                                           double amount,
                                           TransactionType transactionType,
                                           String description,
                                           String referenceNumber,
                                           LocalDateTime createdAt
    ) {
        return new TransactionEntity(
                accountNumber,
                amount,
                createdAt,
                transactionType,
                description,
                referenceNumber);

    }
}