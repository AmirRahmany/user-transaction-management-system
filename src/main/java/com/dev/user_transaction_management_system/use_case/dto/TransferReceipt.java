package com.dev.user_transaction_management_system.use_case.dto;

import java.time.LocalDateTime;

public record TransferReceipt(double amount, String fromAccountNumber, String toAccountNumber,String referenceNumber, LocalDateTime createdAt) {

    public static TransferReceipt makeOf(double amount, String fromAccountNumber, String toAccountNumber,String referenceNumber, LocalDateTime createdAt) {
        return new TransferReceipt(amount,fromAccountNumber,toAccountNumber,referenceNumber,createdAt);
    }
}
