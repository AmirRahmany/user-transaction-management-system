package com.dev.user_transaction_management_system.exceptions;

public class CouldNotFoundUser extends RuntimeException {

    private CouldNotFoundUser() {
    }

    private CouldNotFoundUser(String message) {
        super(message);
    }

    public static CouldNotFoundUser withId(Integer userId) {
        return new CouldNotFoundUser("Couldn't found user with userId: " + userId);
    }
}
