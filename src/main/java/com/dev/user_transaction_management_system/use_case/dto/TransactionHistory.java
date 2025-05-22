package com.dev.user_transaction_management_system.use_case.dto;

import lombok.Builder;

import java.time.LocalDateTime;


public record TransactionHistory(String transactionType,
                                 LocalDateTime dateTime,
                                 double amount,
                                 String referenceNumber) {
}
