package com.dev.user_transaction_management_system.use_case.dto;

public record TransferMoneyRequest(double amount,
                                   String fromAccountNumber,
                                   String toAccountNumber,
                                   String description) {
}
