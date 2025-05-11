package com.dev.user_transaction_management_system.domain.user;

public final class UserId {
    private final int id;

    private UserId(int id) {
        this.id = id;
    }

    public static UserId fromInt(int userId){
        return new UserId(userId);
    }

    public static UserId autoGenerateByDb() {
        return fromInt(0);
    }

    public int toInt(){
        return id;
    }
}
