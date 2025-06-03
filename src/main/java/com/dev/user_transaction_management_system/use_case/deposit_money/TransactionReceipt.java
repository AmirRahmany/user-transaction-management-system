package com.dev.user_transaction_management_system.use_case.deposit_money;

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
