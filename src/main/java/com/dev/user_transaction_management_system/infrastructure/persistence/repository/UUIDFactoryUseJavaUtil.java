package com.dev.user_transaction_management_system.infrastructure.persistence.repository;

import java.util.UUID;

public class UUIDFactoryUseJavaUtil implements IdentifierFactory<UUID> {

    @Override
    public UUID create() {
        return UUID.randomUUID();
    }
}
