package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

public interface IdentifierGenerator<T> {
    T generate();
}
