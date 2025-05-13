package com.dev.user_transaction_management_system.domain.transaction;

import org.springframework.util.Assert;

public final class ReferenceNumber {
    private final String referenceNumber;

    private ReferenceNumber(String referenceNumber) {
        Assert.hasText(referenceNumber, "reference number cannot be null or empty");

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
