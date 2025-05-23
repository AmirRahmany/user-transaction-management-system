package com.dev.user_transaction_management_system.domain.exceptions;

import jakarta.validation.constraints.NotNull;

@NotNull
public class CouldNotFindBankAccount extends RuntimeException {

    public CouldNotFindBankAccount() {
    }

    public CouldNotFindBankAccount(String message) {
        super(message);
    }

    public static CouldNotFindBankAccount withAccountNumber(String accountNumber) {
        final String message = String.format("Could not find an account with account number %s", accountNumber);
        return new CouldNotFindBankAccount(message);
    }
}
