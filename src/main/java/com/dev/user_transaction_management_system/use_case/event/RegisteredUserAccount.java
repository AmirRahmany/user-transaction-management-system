package com.dev.user_transaction_management_system.use_case.event;

import com.dev.user_transaction_management_system.domain.Event;

public record RegisteredUserAccount(String fullName, String email, String phoneNumber) implements Event {

    @Override
    public String getMessage() {
        return String.format("Hi %s\n" + " Your registration was successful.",fullName);
    }
}
