package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

public interface IdentifierFactory<T> {
    T create();
}
