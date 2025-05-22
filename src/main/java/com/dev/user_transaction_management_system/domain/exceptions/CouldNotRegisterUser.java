package com.dev.user_transaction_management_system.domain.exceptions;

public class CouldNotRegisterUser extends RuntimeException {
    public CouldNotRegisterUser() {
    }

    private CouldNotRegisterUser(String message) {
        super(message);
    }

    public static CouldNotRegisterUser becauseUserAlreadyExisted() {
        return new CouldNotRegisterUser();
    }
}
