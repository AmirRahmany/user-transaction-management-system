package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.infrastructure.util.Precondition;

public class TransactionId {
    private final Integer transactionId;

    private TransactionId(Integer transactionId) {
        Precondition.require(transactionId != null);

        this.transactionId = transactionId;
    }

    public static TransactionId fromInt(Integer transactionId) {
        return new TransactionId(transactionId);
    }

    public static TransactionId autoGenerateByDb() {
        return fromInt(0);
    }
}
