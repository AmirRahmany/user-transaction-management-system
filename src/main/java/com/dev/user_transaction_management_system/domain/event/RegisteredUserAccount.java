package com.dev.user_transaction_management_system.domain.event;

import com.dev.user_transaction_management_system.domain.user.Email;

public record RegisteredUserAccount(String fullName,
                                    String receiverEmail,
                                    String phoneNumber) implements NotifiableEvent {

    @Override
    public Subject subject() {
        return Subject.of("Account registered");
    }

    @Override
    public Message message() {
        return Message.of(body());
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }

    private String body() {
        return String.format("Hi %s\n" + " Your registration was successful.", fullName);
    }

}
