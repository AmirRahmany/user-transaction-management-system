package com.dev.user_transaction_management_system.use_case.open_bank_account;

public record AccountRequest(
        String username,
        double balance) {
}
