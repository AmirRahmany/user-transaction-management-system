package com.dev.user_transaction_management_system.use_case.deposit_money;

public record DepositRequest(double amount, String accountNumber, String description) {
}
