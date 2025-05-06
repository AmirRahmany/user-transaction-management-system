package com.dev.user_transaction_management_system.domain.account;

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

    public static AccountNumber of(String accountNumber) {
        return new AccountNumber(accountNumber);
    }

    @Override
    public String toString() {
        return accountNumber;
    }
}
