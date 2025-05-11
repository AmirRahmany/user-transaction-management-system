package com.dev.user_transaction_management_system.use_case.dto;

import com.dev.user_transaction_management_system.domain.transaction.Deposit;
import com.dev.user_transaction_management_system.domain.transaction.Transaction;

import java.time.LocalDateTime;

public record DepositReceipt(String referenceNumber,
                             String fromAccountNumber,
                             String toAccountNumber,
                             LocalDateTime createdAt) {

    public static DepositReceipt makeOf(String referenceNumber,
                                     String fromAccountNumber,
                                     String toAccountNumber,
                                     LocalDateTime createdAt) {
        return new DepositReceipt(referenceNumber,fromAccountNumber,toAccountNumber,createdAt);
    }
}
