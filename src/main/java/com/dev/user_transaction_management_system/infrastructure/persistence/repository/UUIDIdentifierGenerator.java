package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import java.util.UUID;

public class UUIDIdentifierGenerator implements IdentifierGenerator<UUID> {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
