package com.dev.user_transaction_management_system.domain.transaction;

public record TransactionDetail(Amount amount, TransactionType transactionType, String description) {

    public static TransactionDetail of(Amount amount, TransactionType transactionType, String description) {
        return new TransactionDetail(amount, transactionType, description);
    }
}
