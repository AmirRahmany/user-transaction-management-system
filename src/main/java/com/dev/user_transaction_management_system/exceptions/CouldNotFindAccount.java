package com.dev.user_transaction_management_system.exceptions;

import jakarta.validation.constraints.NotNull;

@NotNull
public class CouldNotFindAccount extends RuntimeException {

    public CouldNotFindAccount() {
    }

    public CouldNotFindAccount(String message) {
        super(message);
    }

    public static CouldNotFindAccount withId(Integer accountId) {
        final String message = String.format("Could not find an account with ID %d", accountId);
        return new CouldNotFindAccount(message);
    }
}
