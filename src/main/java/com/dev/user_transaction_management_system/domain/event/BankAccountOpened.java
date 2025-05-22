package com.dev.user_transaction_management_system.domain.event;

import static java.lang.String.format;

public record BankAccountOpened(String fullName,
                                String accountNumber,
                                String toEmail,
                                String phoneNumber) implements NotifiableEvent {


    @Override
    public String getMessage() {
        return format("Dear customer %s,welcome to our bank\n" +
                "your account with number %s successfully opened.",fullName,accountNumber);
    }

    @Override
    public String getSubject() {
        return "Bank account opened";
    }
}
