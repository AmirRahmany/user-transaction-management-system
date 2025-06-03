package com.dev.user_transaction_management_system.use_case.transfer_money;

public record TransferReceipt(double amount,
                              String fromAccountNumber,
                              String toAccountNumber,
                              String referenceNumber,
                              String createdAt) {

    public static TransferReceipt makeOf(double amount,
                                         String fromAccountNumber,
                                         String toAccountNumber,
                                         String referenceNumber,
                                         String createdAt) {
        return new TransferReceipt(amount,fromAccountNumber,toAccountNumber,referenceNumber,createdAt);
    }
}
