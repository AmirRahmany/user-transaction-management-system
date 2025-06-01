package com.dev.user_transaction_management_system.use_case.open_bank_account;

import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;

public record AccountOpenedResponse(
        String accountNumber,
        String fullName,
        Double balance,
        String createdAt,
        AccountStatus status) {
}
