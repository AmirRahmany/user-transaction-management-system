package com.dev.user_transaction_management_system.domain.user;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Password {
    private static final String PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-`~\\[\\]{}|;':\",./<>?]).{8,}$";
    private final String plainPassword;


    private Password(String password) {
        if (password == null || password.isBlank() || isValid(password))
            throw new IllegalArgumentException();

        this.plainPassword = password;
    }


    public static Password of(String password) {
        return new Password(password);
    }

    public boolean isValid(String password) {
        return !password.matches(PATTERN);
    }

    public String toString() {
        return plainPassword;
    }
}
