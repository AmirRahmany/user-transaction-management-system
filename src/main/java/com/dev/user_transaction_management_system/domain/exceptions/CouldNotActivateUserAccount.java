package com.dev.user_transaction_management_system.domain.exceptions;

public class CouldNotActivateUserAccount extends RuntimeException {

    public static CouldNotActivateUserAccount becauseUserAccountIsAlreadyActivated(){
        return new CouldNotActivateUserAccount();
    }
}
