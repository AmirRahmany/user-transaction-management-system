package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.domain.account.AccountNumber;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.TransactionEntity;

import java.time.LocalDateTime;

public class Withdrawal extends Transaction{
    private final AccountNumber accountNumber;


    Withdrawal(TransactionId transactionId,
               AccountNumber accountNumber,
               TransactionDetail transactionDetail,
               ReferenceNumber referenceNumber,
               LocalDateTime createdAt) {

        super(transactionId, transactionDetail, referenceNumber, createdAt);
        this.accountNumber = accountNumber;
    }

    public static Transaction of(
            TransactionId transactionId,
            AccountNumber fromAccountNumber,
            TransactionDetail transactionDetail,
            ReferenceNumber referenceNumber,
            LocalDateTime createdAt) {

        return new Withdrawal(transactionId,
                fromAccountNumber,
                transactionDetail,
                referenceNumber,
                createdAt);
    }

    @Override
    public TransactionEntity toEntity() {
        return TransactionEntity.initOf(
                accountNumber.toString(),
                null,
                transactionDetail.amount().toValue(),
                transactionStatus,
                transactionDetail.transactionType(),
                transactionDetail.description(),
                referenceNumber.toString(),
                createdAt
        );
    }
}
