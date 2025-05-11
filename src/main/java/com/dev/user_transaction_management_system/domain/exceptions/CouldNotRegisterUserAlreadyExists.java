package com.dev.user_transaction_management_system.domain.exceptions;

public class CouldNotRegisterUserAlreadyExists extends RuntimeException {
    public CouldNotRegisterUserAlreadyExists() {
    }

    public CouldNotRegisterUserAlreadyExists(String message) {
        super(message);
    }
}
