package com.dev.user_transaction_management_system.domain.event;

public interface Notifier {

    void send(NotifiableEvent event);
}
