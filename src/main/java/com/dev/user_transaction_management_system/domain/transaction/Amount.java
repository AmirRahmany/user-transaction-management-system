package com.dev.user_transaction_management_system.domain.transaction;

import java.math.BigDecimal;

public class Amount {
    private final double amount;

    private Amount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }

        this.amount = amount;
    }

    public static Amount of(double amount) {
        return new Amount(amount);
    }

    public double toValue(){
        return amount;
    }
}
