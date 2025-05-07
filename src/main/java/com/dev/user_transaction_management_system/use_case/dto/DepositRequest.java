package com.dev.user_transaction_management_system.use_case.dto;

import java.time.LocalDateTime;

public record DepositRequest(double amount,
                             String fromAccountNumber,
                             String toAccountNumber,
                             String description,
                             LocalDateTime createdAt) {
}
