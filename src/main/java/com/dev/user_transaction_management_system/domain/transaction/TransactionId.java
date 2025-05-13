package com.dev.user_transaction_management_system.domain.transaction;

import org.springframework.util.Assert;

public class TransactionId {
    private final Integer transactionId;

    private TransactionId(Integer transactionId) {
        Assert.notNull(transactionId,"transaction id cannot be null");

        this.transactionId = transactionId;
    }

    public static TransactionId fromInt(Integer transactionId) {
        return new TransactionId(transactionId);
    }

    public static TransactionId autoGenerateByDb() {
        return fromInt(0);
    }
}
