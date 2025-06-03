package com.dev.user_transaction_management_system.use_case.withdraw_money;

public record WithdrawalRequest(
        double funds,
        String fromAccountNumber,
        String description) {
}
