package com.dev.user_transaction_management_system.use_case.event;

import com.dev.user_transaction_management_system.domain.NotifiableEvent;

public record BankAccountActivated(String fullName,String accountNumber,String email) implements NotifiableEvent {

    @Override
    public String getMessage() {
        return String.format("Hi %s your account (%s) was successfully activated.",fullName,accountNumber);
    }
}
