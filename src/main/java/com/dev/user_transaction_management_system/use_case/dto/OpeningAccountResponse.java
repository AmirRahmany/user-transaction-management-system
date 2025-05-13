package com.dev.user_transaction_management_system.use_case.dto;

import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;

import java.time.LocalDateTime;

public record OpeningAccountResponse(
         String accountNumber,
         String fullName,
         Double balance,
         LocalDateTime createdAt,
        AccountStatus status) {
}
