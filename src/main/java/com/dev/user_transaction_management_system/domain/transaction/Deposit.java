package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.time.LocalDateTime;

public class Deposit extends Transaction {
    private final AccountNumber fromAccountNumber;
    private final AccountNumber toAccountNumber;

    public Deposit(Integer transactionId,
                   TransactionDetail transactionDetail,
                   AccountNumber fromAccountNumber,
                   AccountNumber toAccountNumber,
                   String referenceNumber,
                   LocalDateTime createdAt) {

        super(transactionId, transactionDetail, referenceNumber, createdAt);
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
    }

    public static Transaction of(
            Integer transactionId,
            TransactionDetail transactionDetail,
            AccountNumber fromAccountNumber,
            AccountNumber toAccountNumber,
            String referenceNumber,
            LocalDateTime createdAt) {

        return new Deposit(transactionId, transactionDetail, fromAccountNumber, toAccountNumber, referenceNumber, createdAt);
    }

    @Override
    public TransactionEntity toEntity() {
        return TransactionEntity.initOf(
                fromAccountNumber.toString(),
                toAccountNumber.toString(),
                transactionDetail.amount().toValue(),
                transactionStatus,
                transactionDetail.transactionType(),
                transactionDetail.description(),
                referenceNumber,
                createdAt
        );
    }
}
