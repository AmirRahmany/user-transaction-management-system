package com.dev.user_transaction_management_system.use_case.dto;

import java.time.LocalDateTime;

public record DepositReceipt(String referenceNumber,
                             String fromAccountNumber,
                             LocalDateTime createdAt) {

    public static DepositReceipt makeOf(String referenceNumber,
                                     String fromAccountNumber,
                                     LocalDateTime createdAt) {
        return new DepositReceipt(referenceNumber,fromAccountNumber,createdAt);
    }
}
