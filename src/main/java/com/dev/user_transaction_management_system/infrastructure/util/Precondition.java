package com.dev.user_transaction_management_system.infrastructure.util;

public class Precondition {

    public static void require(boolean condition) {
        if (!condition)
            throw new IllegalArgumentException();
    }
}
