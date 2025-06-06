package com.dev.user_transaction_management_system.domain.exceptions;

public class UserFriendlyException extends RuntimeException {
    public UserFriendlyException() {
    }

    public UserFriendlyException(String s) {
        super(s);
    }
}
