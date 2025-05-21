package com.dev.user_transaction_management_system.domain;

public interface NotifiableEvent {

    String toEmail();

    String getMessage();

    String getSubject();
}
