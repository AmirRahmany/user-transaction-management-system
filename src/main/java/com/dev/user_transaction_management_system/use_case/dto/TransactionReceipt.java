package com.dev.user_transaction_management_system.use_case.dto;

import java.time.LocalDateTime;

public record TransactionReceipt(
        double amount,
        String referenceNumber,
        String accountNumber,
        LocalDateTime createdAt) {

    public static TransactionReceipt makeOf(
            double amount,
            String referenceNumber,
            String fromAccountNumber,
            LocalDateTime createdAt) {
        return new TransactionReceipt(amount,referenceNumber, fromAccountNumber, createdAt);
    }
}
