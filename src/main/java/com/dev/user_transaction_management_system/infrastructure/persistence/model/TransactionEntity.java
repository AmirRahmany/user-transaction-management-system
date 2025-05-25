package com.dev.user_transaction_management_system.infrastructure.persistence.model;

import com.dev.user_transaction_management_system.domain.Date;
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

}