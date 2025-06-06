package com.dev.user_transaction_management_system.domain.bank_account;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.user.Email;

import static java.lang.String.format;

public record BankAccountWasOpened(String fullName,
                                   String accountNumber,
                                   String receiverEmail,
                                   String phoneNumber) implements NotifiableEvent {


    @Override
    public Message message() {
        return Message.of(body());
    }

    @Override
    public Email to() {
        return Email.of(receiverEmail);
    }

    private String body() {
        return format("Dear customer %s,welcome to our bank\n" +
                "your account with number %s successfully opened.", fullName, accountNumber);
    }


    public Subject subject() {
        return Subject.of("Bank account opened");
    }
}
