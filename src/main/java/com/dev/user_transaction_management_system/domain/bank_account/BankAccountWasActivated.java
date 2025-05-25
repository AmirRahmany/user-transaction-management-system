package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.user.Email;

public record BankAccountWasActivated(String fullName,
                                      String accountNumber,
                                      String receiverEmail) implements NotifiableEvent {

    @Override
    public Message message() {
        return Message.of(body());
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }

    private String body() {
        return String.format("Hi %s your account (%s) was successfully activated.", fullName, accountNumber);
    }

    public Subject subject() {
        return Subject.of("Bank account was activated");
    }
}
