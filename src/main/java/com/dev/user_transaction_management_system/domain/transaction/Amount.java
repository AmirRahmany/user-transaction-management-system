package com.dev.user_transaction_management_system.domain.transaction;

import io.jsonwebtoken.lang.Assert;

import java.util.Objects;

public class Amount {
    public static final double NEGATIVE_AMOUNT = -1;
    private final double amount;

    private Amount(double amount) {
        Assert.gt(amount, NEGATIVE_AMOUNT,"Amount must be positive!");

        this.amount = amount;
    }

    public static Amount of(double amount) {
        return new Amount(amount);
    }

    public double asDouble(){
        return amount;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount1 = (Amount) o;
        return Double.compare(amount, amount1.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public String toString() {
        return "Amount{" +
                "amount=" + amount +
                '}';
    }
}
