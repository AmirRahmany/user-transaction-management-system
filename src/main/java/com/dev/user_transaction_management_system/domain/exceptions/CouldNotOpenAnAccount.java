package com.dev.user_transaction_management_system.domain.exceptions;

public class CouldNotOpenAnAccount extends RuntimeException {

    private CouldNotOpenAnAccount() {
    }

    private CouldNotOpenAnAccount(String message) {
        super(message);
    }

    public static CouldNotOpenAnAccount withDisableUser(){
        return new CouldNotOpenAnAccount("can not open an account for disable user");
    }
}
