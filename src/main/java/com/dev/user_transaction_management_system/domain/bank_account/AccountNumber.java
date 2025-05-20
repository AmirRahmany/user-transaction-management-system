package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotProcessTransaction;
import org.springframework.util.Assert;

import java.util.Objects;

import static java.lang.String.format;

public class AccountNumber {

    private final int ACCOUNT_NUMBER_LENGTH = 13;

    private final String accountNumber;

    private AccountNumber(String accountNumber) {
        Assert.hasText(accountNumber,"bank account number cannot be null or empty");

        if (!hasMinimumLength(accountNumber)) {
            String message = format("bank account number length should be equal with: %s" ,ACCOUNT_NUMBER_LENGTH);
            throw new IllegalArgumentException(message);
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

    public void ensureDistinctAccounts(AccountNumber target) {
        if (isSameAs(target)) {
            throw CouldNotProcessTransaction.becauseSourceAndTargetAccountsAreTheSame();
        }
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

    @Override
    public String toString() {
        return accountNumber;
    }

    public String asString() {
        return accountNumber;
    }
}
