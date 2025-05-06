package com.dev.user_transaction_management_system.dto;

import com.dev.user_transaction_management_system.domain.transaction.AccountStatus;

import java.time.LocalDateTime;

public record AccountResponse(
         String accountNumber,
         String fullName,
         Double balance,
         LocalDateTime createdAt,
        AccountStatus status) {
}
