package com.dev.user_transaction_management_system.use_case.event;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;

public record RegisteredUserAccount(String fullName, String toEmail, String phoneNumber) implements NotifiableEvent {

    @Override
    public String getMessage() {
        return String.format("Hi %s\n" + " Your registration was successful.",fullName);
    }

    @Override
    public String getSubject() {
        return "Account registered";
    }
}
