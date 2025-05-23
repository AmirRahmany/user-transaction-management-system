package com.dev.user_transaction_management_system.domain.event;

public record Message(String body) {
    public static Message of(String body) {
        return new Message(body);
    }
}
