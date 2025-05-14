package com.dev.user_transaction_management_system.domain.user;

import java.util.UUID;

public final class UserId {
    private final UUID id;

    private UserId(UUID id) {
        this.id = id;
    }

    public static UserId fromUUID(UUID userId){
        return new UserId(userId);
    }

    public static UserId fromString(String id) {
        return new UserId(UUID.fromString(id));
    }


    public String asString(){
        return id.toString();
    }

    @Override
    public String toString() {
        return "UserId{" +
                "id=" + id +
                '}';
    }
}
