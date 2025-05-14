package com.dev.user_transaction_management_system.use_case.dto;

public record AccountRequest(
        String userId,
        double balance) {
}
