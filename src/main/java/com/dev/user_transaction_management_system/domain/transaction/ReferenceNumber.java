package com.dev.user_transaction_management_system.domain.transaction;

import com.dev.user_transaction_management_system.infrastructure.util.Precondition;

public final class ReferenceNumber {
    private final String referenceNumber;

    private ReferenceNumber(String referenceNumber) {
        Precondition.require(referenceNumber != null);

        this.referenceNumber = referenceNumber;
    }

    public static ReferenceNumber fromString(String referenceNumber) {
        return new ReferenceNumber(referenceNumber);
    }

    @Override
    public String toString() {
        return referenceNumber;
    }
}
