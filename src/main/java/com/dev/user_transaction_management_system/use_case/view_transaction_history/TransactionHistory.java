package com.dev.user_transaction_management_system.use_case.view_transaction_history;


public record TransactionHistory(String transactionType,
                                 String dateTime,
                                 double amount,
                                 String referenceNumber) {
}
