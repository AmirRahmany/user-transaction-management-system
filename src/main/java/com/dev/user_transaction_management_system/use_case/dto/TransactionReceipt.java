package com.dev.user_transaction_management_system.use_case.dto;

public record TransactionReceipt(
        double amount,
        String referenceNumber,
        String accountNumber,
        String createdAt) {

    public static TransactionReceipt makeOf(
            double amount,
            String referenceNumber,
            String fromAccountNumber,
            String createdAt) {
        return new TransactionReceipt(amount,referenceNumber, fromAccountNumber, createdAt);
    }
}
