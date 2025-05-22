package com.dev.user_transaction_management_system.domain.event;

public record BankAccountActivated(String fullName,String accountNumber,String toEmail) implements NotifiableEvent {

    @Override
    public String getMessage() {
        return String.format("Hi %s your account (%s) was successfully activated.",fullName,accountNumber);
    }

    @Override
    public String getSubject() {
        return "Bank account was activated";
    }
}
