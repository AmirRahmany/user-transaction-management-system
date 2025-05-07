package com.dev.user_transaction_management_system.use_case.dto;

import com.dev.user_transaction_management_system.domain.account.Account;
import com.dev.user_transaction_management_system.domain.transaction.Amount;

public record WithdrawalRequest(
        Amount funds,
        Account account,
        String description) {
}
