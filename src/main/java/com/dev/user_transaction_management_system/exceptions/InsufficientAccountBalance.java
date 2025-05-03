package com.dev.user_transaction_management_system.exceptions;

public class InsufficientAccountBalance extends RuntimeException {
    public InsufficientAccountBalance() {

    }

    public InsufficientAccountBalance(String message) {
        super(message);
    }
}
