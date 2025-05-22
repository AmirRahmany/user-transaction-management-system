package com.dev.user_transaction_management_system.domain.event;

public interface NotifiableEvent {

    String toEmail();

    String getMessage();

    String getSubject();
}
