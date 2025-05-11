package com.dev.user_transaction_management_system.domain.exceptions;

import jakarta.validation.constraints.NotNull;

@NotNull
public class CouldNotFindAccount extends RuntimeException {

    public CouldNotFindAccount() {
    }

    public CouldNotFindAccount(String message) {
        super(message);
    }

    public static CouldNotFindAccount withAccountNumber(String accountNumber) {
        final String message = String.format("Could not find an account with account number %s", accountNumber);
        return new CouldNotFindAccount(message);
    }
}
