package com.dev.user_transaction_management_system.exceptions;

public class CanNotConnectToDatabase extends RuntimeException {

    public CanNotConnectToDatabase() {
    }

    public CanNotConnectToDatabase(String message) {
        super(message);
    }
}
