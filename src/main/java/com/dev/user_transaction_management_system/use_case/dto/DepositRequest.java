package com.dev.user_transaction_management_system.use_case.dto;

public record DepositRequest(double amount, String accountNumber, String description) {
}
