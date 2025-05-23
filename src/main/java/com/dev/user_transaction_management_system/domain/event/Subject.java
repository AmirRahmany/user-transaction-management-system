package com.dev.user_transaction_management_system.domain.event;

public record Subject(String subject) {
    public static Subject of(String subject) {
        return new Subject(subject);
    }
}
