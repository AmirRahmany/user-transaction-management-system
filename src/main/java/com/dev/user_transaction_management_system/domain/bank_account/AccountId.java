package com.dev.user_transaction_management_system.domain.bank_account;

import io.jsonwebtoken.lang.Assert;

import java.util.UUID;

public final class AccountId {
    private final UUID id;

    public AccountId(UUID id) {
        Assert.notNull(id,"user id cannot be null");
        this.id = id;
    }

    public static AccountId fromUUID(UUID accountId){
        return new AccountId(accountId);
    }


    public String asString() {
        return id.toString();
    }
}
