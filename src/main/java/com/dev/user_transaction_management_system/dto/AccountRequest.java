package com.dev.user_transaction_management_system.dto;

public record AccountRequest(
        Integer userId,
        double balance) {
}
