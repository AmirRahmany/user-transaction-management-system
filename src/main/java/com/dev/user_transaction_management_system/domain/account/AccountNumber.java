package com.dev.user_transaction_management_system.domain.account;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;

import java.util.Objects;

public class AccountNumber {

    private final int ACCOUNT_NUMBER_LENGTH = 13;

    private final String accountNumber;

    private AccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("BankAccount number can't be null or empty!");

        if (!hasMinimumLength(accountNumber)) {
            throw new IllegalArgumentException("account number length should be equal with: " + ACCOUNT_NUMBER_LENGTH);
        }

        this.accountNumber = accountNumber;
    }

    private boolean hasMinimumLength(String accountNumber) {
        return accountNumber.length() == ACCOUNT_NUMBER_LENGTH;
    }

    public static AccountNumber of(String accountNumber) {
        return new AccountNumber(accountNumber);
    }

    public boolean isSameAs(AccountNumber toAccountNumber) {
        return accountNumber.equals(toAccountNumber.toString());
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
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ACCOUNT_NUMBER_LENGTH, accountNumber);
    }

    public void ensureDistinctAccounts(AccountNumber target) {
        if (isSameAs(target)) {
            throw CouldNotProcessTransaction.becauseSourceAndTargetAccountsAreTheSame();
        }
    }
}
