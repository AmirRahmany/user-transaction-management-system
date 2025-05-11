package com.dev.user_transaction_management_system.domain.account;

public final class AccountId {
    public static final int NEW_ACCOUNT = 0;
    private final int id;

    public AccountId(int id) {
        this.id = id;
    }

    public static AccountId fromInt(int accountId){
        return new AccountId(accountId);
    }

    public static AccountId newAccount() {
        return fromInt(NEW_ACCOUNT);
    }

    public Integer toInt() {
        return id;
    }
}
