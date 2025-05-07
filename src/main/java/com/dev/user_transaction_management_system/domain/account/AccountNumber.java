package com.dev.user_transaction_management_system.domain.account;

import java.util.Objects;

public class AccountNumber {

    private final int ACCOUNT_NUMBER_LENGTH = 13;

    private final String accountNumber;

    private AccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number can't be null or empty!");

        if (accountNumber.length() != ACCOUNT_NUMBER_LENGTH) {
            throw new IllegalArgumentException("account number length should be equal with: " + ACCOUNT_NUMBER_LENGTH);
        }

        this.accountNumber = accountNumber;
    }

    public boolean isSameAs(AccountNumber toAccountNumber) {
        return accountNumber.equals(toAccountNumber.toString());
    }

    public static AccountNumber of(String accountNumber) {
        return new AccountNumber(accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountNumber that = (AccountNumber) o;
        return ACCOUNT_NUMBER_LENGTH == that.ACCOUNT_NUMBER_LENGTH && Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ACCOUNT_NUMBER_LENGTH, accountNumber);
    }
}
