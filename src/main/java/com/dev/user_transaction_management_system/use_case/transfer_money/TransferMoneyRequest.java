package com.dev.user_transaction_management_system.use_case.transfer_money;

public record TransferMoneyRequest(double amount,
                                   String fromAccountNumber,
                                   String toAccountNumber,
                                   String description) {
}
