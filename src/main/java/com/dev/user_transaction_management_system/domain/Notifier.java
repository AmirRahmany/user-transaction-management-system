package com.dev.user_transaction_management_system.domain;

public interface Notifier {

    void send(String message,String toEmail);
}
