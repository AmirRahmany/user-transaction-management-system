package com.dev.user_transaction_management_system.use_case.event;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;

import static java.lang.String.format;

public record BankAccountOpened(String fullName,
                                String accountNumber,
                                String email,
                                String phoneNumber) implements NotifiableEvent {


    @Override
    public String getMessage() {
        return format("Dear customer %s,welcome to our bank\n" +
                "your account with number %s successfully opened.",fullName,accountNumber);
    }
}
