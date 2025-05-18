package com.dev.user_transaction_management_system.domain.exceptions;

public class CouldNotFoundUser extends RuntimeException {

    private CouldNotFoundUser() {
    }

    private CouldNotFoundUser(String message) {
        super(message);
    }

    public static CouldNotFoundUser withId(String userId) {
        return new CouldNotFoundUser("Couldn't found user with userId: " + userId);
    }

    public static CouldNotFoundUser withEmail(String email) {
        return new CouldNotFoundUser("Couldn't found user with email: " + email);
    }
}
