package com.dev.user_transaction_management_system.exceptions;

public class CouldNotProcessTransaction extends RuntimeException {
    public CouldNotProcessTransaction() {

    }

    public CouldNotProcessTransaction(String message) {
        super(message);
    }

    public static CouldNotProcessTransaction becauseInsufficientBalance() {
        return new CouldNotProcessTransaction("This transaction cannot be performed.Because insufficient balance.");
    }

    public static CouldNotProcessTransaction becauseSourceAndTargetAccountsAreTheSame() {
        return new CouldNotProcessTransaction("Cannot perform a deposit into the same account it originates from.");
    }
}
