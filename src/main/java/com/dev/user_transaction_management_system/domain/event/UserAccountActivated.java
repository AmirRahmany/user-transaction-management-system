package com.dev.user_transaction_management_system.domain.event;

import com.dev.user_transaction_management_system.domain.user.Email;

public record UserAccountActivated(String fullName,
                                   String receiverEmail,
                                   String phoneNumber) implements NotifiableEvent {

    @Override
    public Subject subject()
    {
        return Subject.of("Your account activated");
    }

    @Override
    public Message message()
    {
        return Message.of(body());
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }

    private String body(){
        return String.format("Hi %s\n" + " Your registration was successful.",fullName);
    }
}
